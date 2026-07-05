---
name: gatling-weights-to-profileconfig
description: >-
  Заменяет хардкод весов randomSwitch в Gatling-сценариях на вызовы
  ProfileConfig.getWeight, чтобы веса подтягивались из профиля. Использовать, когда
  нужно «вынести веса в параметры», «подтягивать веса из profile.yaml» или
  отрефакторить new Choice.WithWeight(...). Опирается на скрипт weights_codemod.py.
---

# Вынос весов randomSwitch в ProfileConfig

Не редактируй веса вручную — это механическая правка, которую надёжно делает
скрипт. Твоя роль: запустить codemod и проверить результат.

Сначала прочитай контракт имён: [gatling-profile-conventions.md](gatling-profile-conventions.md).

## Что делает codemod

```
new Choice.WithWeight(25, exec(LicensesCase.UC01_POST_Licenses_Summary))
```
→
```
new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC01_POST_Licenses_Summary", 25), exec(LicensesCase.UC01_POST_Licenses_Summary))
```

- choice-ключ = **имя переменной** (после точки в `exec(Case.VAR)` или сам
  идентификатор в форме `WithWeight(96, VAR)`).
- старый числовой вес → **дефолт** (последний аргумент).
- добавляет `private static final String SCN = "..."` (из имени класса) и
  `import config.ProfileConfig;`, если их нет.
- пропускает закомментированные `//` строки и уже вынесенные веса (идемпотентно).

## Чек-лист (копируй и отмечай)

```
- [ ] 1. Прогнать dry-run и оценить число замен
- [ ] 2. Применить codemod
- [ ] 3. Проверить, что не осталось числовых WithWeight
- [ ] 4. Проверить SCN и совпадение с injection.scenarios в profile.yaml
- [ ] 5. Скомпилировать (mvn test-compile)
```

**1. Dry-run** (из каталога `gatling/gatlingScripts`):
```bash
python3 ltAuto/weights_codemod.py src/test/java/scenarios/pprbSberrating/LicensesScenario.java --dry-run
```

**2. Применить** (можно каталог целиком — обработает все .java):
```bash
python3 ltAuto/weights_codemod.py src/test/java/scenarios/pprbSberrating/LicensesScenario.java
# или все сценарии домена:
python3 ltAuto/weights_codemod.py src/test/java/scenarios/pprbSberrating
```
Если имя SCN нельзя вывести из имени класса — задай явно: `--scenario-name Licenses`.

**3. Верификация — не должно остаться числовых весов:**
```bash
grep -nE 'new Choice\.WithWeight\(\s*[0-9]' src/test/java/scenarios/**/ *.java || echo "OK: числовых весов нет"
```

**4. Совпадение SCN** с профилем: значение `SCN` в классе == ключ
`injection.scenarios.<SCN>` в `profiles/profile.yaml`. Если сценариев несколько —
у каждого свой `SCN`.

**5. Компиляция:**
```bash
mvn -q -DskipTests test-compile
```
Если падает — почини импорт/`SCN` и повтори. **Не продолжай, пока не компилируется.**

## Пример «было → стало» (эталон)

Было:
```java
randomSwitch().on(
    new Choice.WithWeight(96, UC01_POST_Licenses_Summary),
    new Choice.WithWeight(1,  UC02_POST_Licenses_List)
)
```
Стало:
```java
randomSwitch().on(
    new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC01_POST_Licenses_Summary", 96), UC01_POST_Licenses_Summary),
    new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC02_POST_Licenses_List", 1),  UC02_POST_Licenses_List)
)
```

## Ограничения

- Каждый `Choice.WithWeight(...)` должен быть на одной строке (обычный стиль).
- Если 2-й аргумент — сложное выражение (не `VAR` и не `exec(Case.VAR)`), строка
  пропускается с предупреждением — обработай её вручную по эталону.
