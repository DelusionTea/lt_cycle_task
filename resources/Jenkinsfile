pipeline {
    agent {
        node {
            label 'sberlinux&&Linux_Default'
        }
    }
    environment {
        MAVEN_HOME = "/home/pprb_test/apache-maven-3.9.6/"
        GATLING_IS_RUNNING = "false"
        // Константы для nohup
        REMOTE_RUNLOG     = "/home/pprb_test/gatling_run.log"
        REMOTE_PIDFILE    = "/home/pprb_test/gatling_maven.pid"
        REMOTE_WPIDFILE   = "/home/pprb_test/gatling_wrapper.pid" // PID самого враппера
        REMOTE_STATUSFILE = "/home/pprb_test/gatling_status.txt"
        REMOTE_LOCKFILE   = "/home/pprb_test/lockfileGatling.txt"
        REMOTE_WRAPPER    = "/home/pprb_test/gatling_wrapper.sh"
    }
    parameters {
        choice(name: 'ACTION', choices: ['ЗАПУСТИТЬ ТЕСТ', 'ТОЛЬКО ОБНОВИТЬ СКРИПТЫ (Без запуска)'], description: 'Что делаем?')
        string(name: 'START_TIME', defaultValue: 'now', description: 'Время старта: now (сразу) или HH:mm (например, 23:30). Тест пойдет через nohup.')
        gitParameter(
            name: 'BRANCH',
            branchFilter: 'origin/(.*)',
            defaultValue: 'master',
            description: 'Выберите ветку, из которой выкачивать скрипты Gatling',
            type: 'PT_BRANCH',
            sortMode: 'ASCENDING_SMART',
            selectedValue: 'DEFAULT',
            useRepository: '.*'
        )
        string(name: 'packageSimulation', defaultValue: '', description: 'Впиши класс симуляции руками (например: PPRB_Arbitr.PPRB_Arbitr_Debug). ЕСЛИ ОЧИСТИТЬ ПОЛЕ - включится выбор из списков!')
        credentials(name: 'CREDS', defaultValue: '', description: 'Ключи от тачки', credentialType: 'Username with password', required: true)
    }
    options {
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
        stage('Чекаут нужной ветки') {
            steps {
                echo "Выкачиваем ветку: ${params.BRANCH}"
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "${params.BRANCH}"]],
                    extensions: [
                        [$class: 'CleanBeforeCheckout'],
                        [$class: 'CloneOption', shallow: true, depth: 1, noTags: true]
                    ],
                    userRemoteConfigs: scm.userRemoteConfigs
                ])
            }
        }
        stage('Выбираем пакет и класс симуляции') {
            when
            {
                expression {
                    params.ACTION == 'ЗАПУСТИТЬ ТЕСТ' &&
                    (params.packageSimulation == null || params.packageSimulation.trim() == '')
                }
            }
            steps {
                script {
                    echo "Ручной ввод пуст. Ищем пакеты (АС) в simulations..."
                    def foundDirs = sh(
                        script: '''#!/bin/bash
                        found_any="false"
                        for sim_dir in $(find . -type d -iname "simulations"); do
                            if [[ "$sim_dir" != *"/target/"* ]]; then
                                cd "$sim_dir" || continue
                                find . -mindepth 1 -maxdepth 1 -type d | sed 's|^\\./||'
                                cd - > /dev/null
                                found_any="true"
                            fi
                        done
                        if [ "$found_any" = "false" ]; then
                            echo "ERROR_NO_DIR"
                        fi
                        ''',
                        returnStdout: true
                    ).trim().split('\n')
                    def packageList = []
                    for (String dir : foundDirs) {
                        dir = dir.trim()
                        if (dir != "" && dir != "ERROR_NO_DIR") packageList.add(dir)
                    }
                    if (packageList.isEmpty()) packageList = ["ОШИБКА: НЕТ ПАПОК"]
                    env.SELECTED_PACKAGE = input(
                        message: 'АВТОВЫБОР (Шаг 1 из 2): Выберите пакет (АС):',
                        parameters: [
                            choice(name: 'PACK', choices: packageList.unique().sort(), description: 'Список АС')
                        ]
                    )
                    echo "Ищем скрипты в пакете ${env.SELECTED_PACKAGE}..."
                    def foundFiles = sh(
                        script: '''#!/bin/bash
                        found_any="false"
                        for sim_dir in $(find . -type d -iname "simulations"); do
                            if [[ "$sim_dir" != *"/target/"* ]]; then
                                if [ -d "$sim_dir/''' + env.SELECTED_PACKAGE + '''" ]; then
                                    cd "$sim_dir/''' + env.SELECTED_PACKAGE + '''" || continue
                                    find . -type f \\( -name "*.java" -o -name "*.scala" \\) | sed 's|^\\./||' | sed 's/\\.java$//' | sed 's/\\.scala$//'
                                    cd - > /dev/null
                                    found_any="true"
                                fi
                            fi
                        done
                        if [ "$found_any" = "false" ]; then
                            echo "ERROR_NO_DIR"
                        fi
                        ''',
                        returnStdout: true
                    ).trim().split('\n')
                    def classList = []
                    for (String file : foundFiles) {
                        file = file.trim()
                        if (file != "" && file != "ERROR_NO_DIR") classList.add(file)
                    }
                    if (classList.isEmpty()) classList = ["ОШИБКА: ПУСТАЯ ПАПКА"]
                    env.SELECTED_CLASS = input(
                        message: "АВТОВЫБОР (Шаг 2 из 2): Выберите класс симуляции для [${env.SELECTED_PACKAGE}]:",
                        parameters: [
                            choice(name: 'SIMCLASS', choices: classList.unique().sort(), description: 'Скрипты внутри пакета')
                        ]
                    )
                    env.SELECTED_SIMCLASS = "${env.SELECTED_PACKAGE}.${env.SELECTED_CLASS}"
                    echo "Выбрана симуляция: ${env.SELECTED_SIMCLASS}"
                }
            }
        }
        stage('Подготовка к запуску') {
            when {
                expression { params.ACTION == 'ЗАПУСТИТЬ ТЕСТ' }
            }
            steps {
                script {
                    def finalTarget = ""
                    if (params.packageSimulation != null && params.packageSimulation.trim() != '') {
                        finalTarget = params.packageSimulation.trim()
                        echo "Используется РУЧНОЙ ввод: simulations.${finalTarget}"
                    } else {
                        finalTarget = env.SELECTED_SIMCLASS
                        echo "Используется выбор из ИНТЕРАКТИВНОГО МЕНЮ: simulations.${finalTarget}"
                    }
                    withCredentials([usernamePassword(
                        credentialsId: params.CREDS,
                        usernameVariable: 'remoteHost',
                        passwordVariable: 'remotePassword',
                    )]) {
                        def lockCheckScript = '''#!/bin/bash
                            if [ -f "/home/pprb_test/lockfileGatling.txt" ]; then
                                cat /home/pprb_test/lockfileGatling.txt 2>/dev/null || echo 0
                            else
                                echo 0
                            fi
                        '''
                        def lockValue = sh(
                            script: "sshpass -p '${remotePassword}' ssh -o StrictHostKeyChecking=no ${remoteHost} '${lockCheckScript}'",
                            returnStdout: true
                        ).trim()
                        if (lockValue != "0") {
                            def userChoice = input(
                                message: "⚠️ Генератор ${remoteHost} сейчас ЗАНЯТ (lockfile = ${lockValue}). Что делаем?",
                                parameters: [
                                    choice(name: 'WAIT_OR_ABORT', choices: ['Подождать (встать в очередь)', 'Отменить запуск'], description: 'Выберите действие')
                                ]
                            )
                            if (userChoice == 'Отменить запуск') {
                                currentBuild.result = 'ABORTED'
                                error "Запуск отменён пользователем: генератор занят."
                            } else {
                                echo "Ожидаем освобождения генератора..."
                                timeout(time: 60, unit: 'HOURS') {
                                    waitUntil {
                                        def currentLock = sh(
                                            script: "sshpass -p '${remotePassword}' ssh -o StrictHostKeyChecking=no ${remoteHost} '${lockCheckScript}'",
                                            returnStdout: true
                                        ).trim()
                                        if (currentLock == "0") {
                                            echo "✅ Генератор освободился. Продолжаем запуск."
                                            return true
                                        }
                                        echo "Генератор всё ещё занят (lockfile = ${currentLock}). Повторная проверка через 30 сек..."
                                        sleep 30
                                        return false
                                    }
                                }
                            }
                        } else {
                            echo "✅ Генератор свободен. Начинаем подготовку."
                        }
                        def libsExist = sh(
                            script: "sshpass -p '${remotePassword}' ssh -o StrictHostKeyChecking=no ${remoteHost} '[ -d gatling_job/libs ] && echo \"true\" || echo \"false\"'",
                            returnStdout: true
                        ).trim()
                        if (libsExist == "false") {
                            echo "📦 Либы не найдены на сервере. Копируем..."
                            sh "sshpass -p '${remotePassword}' ssh -o StrictHostKeyChecking=no ${remoteHost} 'mkdir -p gatling_job/gatlingScripts gatling_job/libs'"
                            sh "sshpass -p '${remotePassword}' rsync -avz --delete -e 'ssh -o StrictHostKeyChecking=no' gatling/libs/ ${remoteHost}:gatling_job/libs/ || true"
                        } else {
                            echo "✅ Либы уже на сервере (gatling_job/libs), пропускаем rsync либ."
                            sh "sshpass -p '${remotePassword}' ssh -o StrictHostKeyChecking=no ${remoteHost} 'mkdir -p gatling_job/gatlingScripts'"
                        }
                        sh "sshpass -p '${remotePassword}' rsync -avz --delete -e 'ssh -o StrictHostKeyChecking=no' gatling/gatlingScripts/ ${remoteHost}:gatling_job/gatlingScripts/ || true"
                        env.FINAL_TARGET = finalTarget
                        env.REMOTE_HOST = remoteHost
                        env.REMOTE_PASSWORD = remotePassword
                        env.GATLING_IS_RUNNING = "true"
                        currentBuild.displayName = "${remoteHost} | ${finalTarget}"
                        currentBuild.description = "Gatling host: ${remoteHost}\nSimulation: ${finalTarget}\nStart: ${params.START_TIME}"
                    }

                    // Очистка ненужных папок на удалённой машине
                    echo "=== Очистка ненужных папок на удалённой машине ==="
                    script {
                        def classNameParts = finalTarget.split('\\.')
                        def folderName = classNameParts[0]

                        echo "Целевой класс симуляции: ${finalTarget}"
                        echo "Имя папки для сохранения: ${folderName}"

                        // Если package имеет префикс efs, сохраняем все связанные EFS папки
                        def additionalFoldersToKeep = ""
                        if (folderName.toLowerCase().startsWith('efs')) {
                            echo "Обнаружен префикс 'efs' в package. Будут сохранены дополнительные папки: efsSberbusinessAuth, efsSberbusiness, pprbSberrating, misc"
                            additionalFoldersToKeep = "efsSberbusinessAuth efsSberbusiness"
                        }

                        // Копируем скрипт очистки на удалённую машину
                        sh "sshpass -p '${env.REMOTE_PASSWORD}' rsync -avz -e 'ssh -o StrictHostKeyChecking=no' gatlingJenkins/cleanup_java_dirs.sh ${env.REMOTE_HOST}:/home/pprb_test/cleanup_java_dirs.sh"

                        // Вызываем bash скрипт для очистки на удалённой машине
                        sh "sshpass -p '${env.REMOTE_PASSWORD}' ssh -o StrictHostKeyChecking=no ${env.REMOTE_HOST} 'bash /home/pprb_test/cleanup_java_dirs.sh ${folderName} \"${additionalFoldersToKeep}\"'"

                        // Удаляем скрипт очистки с удалённой машины после использования
                        sh "sshpass -p '${env.REMOTE_PASSWORD}' ssh -o StrictHostKeyChecking=no ${env.REMOTE_HOST} 'rm -f /home/pprb_test/cleanup_java_dirs.sh'"
                    }
                }
            }
        }
        stage('Запуск теста') {
            when {
                expression { params.ACTION == 'ЗАПУСТИТЬ ТЕСТ' }
            }
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: params.CREDS,
                        usernameVariable: 'remoteHost',
                        passwordVariable: 'remotePassword',
                    )]) {
                        def wrapperContent = """#!/bin/bash
# Направляем вывод в лог
exec > ${REMOTE_RUNLOG} 2>&1
echo "--- Wrapper Start: \$(date) ---"
# Сохраняем PID самого враппера
echo \$\$ > ${REMOTE_WPIDFILE}
# Ожидание расписания (HH:mm)
if [ "${params.START_TIME}" != "now" ]; then
    echo "Waiting for scheduled start at ${params.START_TIME}..."
    while [ "\$(date +%H:%M)" != "${params.START_TIME}" ]; do
        sleep 15
    done
fi
echo "STARTED \$(date) simulation=simulations.${env.FINAL_TARGET}" > ${REMOTE_STATUSFILE}
echo 1 > ${REMOTE_LOCKFILE}
cd /home/pprb_test/gatling_job/gatlingScripts || exit 1
source ~/.bash_profile 2>/dev/null || true
source ~/.bashrc 2>/dev/null || true
export MAVEN_OPTS="-Dmaven.settings.file=/home/pprb_test/.m2/settings.xml"
# ПОВЫШАЕМ ЛОГИРОВАНИЕ (только ERROR), чтобы не забивать nohup.log
export LOG_LEVEL=ERROR
export GATLING_CHROME_OPTIONS="--log-level=ERROR"
CLASS="${env.FINAL_TARGET}"
[[ \$CLASS != simulations.* ]] && CLASS="simulations.\$CLASS"
echo "Launching Maven via nohup for class: \$CLASS"
# Запуск Maven: только ошибки в nohup.out
nohup /home/pprb_test/apache-maven-3.9.6/bin/mvn gatling:test -B -q \\
    -s /home/pprb_test/.m2/settings.xml \\
    -D"gatling.simulationClass=\$CLASS" \\
    -Dgatling.resultsFolder=results \\
    -Dmaven.repo.local=/home/pprb_test/.m2 \\
    -D"logback.level.root=ERROR" \\
    -D"logback.level.io.gatling=ERROR" \\
    -D"jenkins.build.tag=${env.BUILD_TAG}" \\
    -Dgatling.core.directory.results=/home/pprb_test/gatling_job/gatlingScripts/results \\
    > /dev/null 2> /home/pprb_test/nohup.out &
MAVEN_PID=\$!
echo \$MAVEN_PID > ${REMOTE_PIDFILE}
echo "Maven started with PID: \$MAVEN_PID (BuildTag: ${env.BUILD_TAG})"
wait \$MAVEN_PID
MAVEN_EXIT=\$?
# Cleanup
echo 0 > ${REMOTE_LOCKFILE}
rm -f ${REMOTE_PIDFILE} ${REMOTE_WPIDFILE}
rm -f /home/pprb_test/gatling_report.tar.gz
if cd results 2>/dev/null; then
    LATEST=\$(ls -td -- */ 2>/dev/null | head -n 1)
    [ -n "\$LATEST" ] && tar -czf /home/pprb_test/gatling_report.tar.gz "\$LATEST"
fi
if [ \$MAVEN_EXIT -eq 0 ]; then
    echo "SUCCESS \$(date)" > ${REMOTE_STATUSFILE}
else
    echo "FAILED exit_code=\$MAVEN_EXIT \$(date)" > ${REMOTE_STATUSFILE}
fi
"""
                        writeFile file: 'gatling_wrapper.sh', text: wrapperContent
                        sh "sshpass -p '${remotePassword}' scp -o StrictHostKeyChecking=no gatling_wrapper.sh ${remoteHost}:${REMOTE_WRAPPER}"
                        sh "sshpass -p '${remotePassword}' ssh -o StrictHostKeyChecking=no ${remoteHost} 'tr -d \"\\r\" < ${REMOTE_WRAPPER} > ${REMOTE_WRAPPER}.tmp && mv ${REMOTE_WRAPPER}.tmp ${REMOTE_WRAPPER} && chmod +x ${REMOTE_WRAPPER}'"
                        sh "sshpass -p '${remotePassword}' ssh -o StrictHostKeyChecking=no ${remoteHost} 'nohup ${REMOTE_WRAPPER} > /home/pprb_test/nohup_wrapper.log 2>&1 &'"
                        echo "✅ Тест запущен. Мониторинг..."
                        def finished = false
                        timeout(time: 60, unit: 'HOURS') {
                            while (!finished) {
                                script {
                                    def status = sh(
                                        script: "sshpass -p '${remotePassword}' ssh -o StrictHostKeyChecking=no ${remoteHost} 'cat ${env.REMOTE_STATUSFILE} 2>/dev/null || echo \"RUNNING\"'",
                                        returnStdout: true
                                    ).trim()
                                    if (status.contains("SUCCESS") || status.contains("FAILED")) {
                                        echo "Тест завершен: ${status}"
                                        finished = true
                                        if (status.contains("FAILED")) {
                                            currentBuild.result = 'FAILURE'
                                            echo "=== MAVEN LOG SNIPPET (last 50 lines) ==="
                                            sh "sshpass -p '${remotePassword}' ssh -o StrictHostKeyChecking=no ${remoteHost} 'tail -n 50 /home/pprb_test/nohup.out'"
                                        }
                                    } else {
                                        echo "Тест еще в процессе... (Status: ${status})"
                                        sleep 60
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        stage('Сохранение отчёта') {
            when {
                expression { params.ACTION == 'ЗАПУСТИТЬ ТЕСТ' }
            }
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: params.CREDS,
                        usernameVariable: 'remoteHost',
                        passwordVariable: 'remotePassword',
                    )]) {
                        echo "Забираем отчет с сервера..."
                        sh "sshpass -p '${remotePassword}' scp -o StrictHostKeyChecking=no ${remoteHost}:/home/pprb_test/gatling_report.tar.gz gatling_report.tar.gz || echo 'Отчет не найден'"
                    }
                }
                archiveArtifacts artifacts: 'gatling_report.tar.gz', allowEmptyArchive: true
            }
        }
    }
    post {
        aborted {
            script {
                if (env.REMOTE_HOST?.trim() && env.FINAL_TARGET?.trim()) {
                    echo "POST(aborted): cleanup по simulationClass=${env.FINAL_TARGET} на ${env.REMOTE_HOST}..."
                    try {
                        withCredentials([usernamePassword(
                            credentialsId: params.CREDS,
                            usernameVariable: 'remoteHost',
                            passwordVariable: 'remotePassword',
                        )]) {
                            withEnv([
                                "RH=${remoteHost}",
                                "RP=${remotePassword}",
                                "FT=${env.FINAL_TARGET}",
                                "RWPID=${env.REMOTE_WPIDFILE}",
                                "RPID=${env.REMOTE_PIDFILE}",
                                "RLOCK=${env.REMOTE_LOCKFILE}",
                                "BT=${env.BUILD_TAG}"
                            ]) {
                                sh '''
                                    sshpass -p "$RP" ssh -o StrictHostKeyChecking=no "$RH" '
                                        set +e
                                        echo "=== Emergency Cleanup Start [$BT] ==="
                                        killtree() {
                                          _pid="$1"
                                          [ -z "$_pid" ] && return 0
                                          for _child in $(pgrep -P "$_pid" 2>/dev/null); do
                                            killtree "$_child"
                                          done
                                          kill -15 "$_pid" 2>/dev/null || true
                                          sleep 1
                                          kill -9 "$_pid" 2>/dev/null || true
                                        }
                                        PATTERN1="gatling.simulationClass=simulations.$FT"
                                        PATTERN2="gatling.simulationClass=$FT"
                                        PIDS="$(pgrep -f "$PATTERN1" 2>/dev/null || true)"
                                        if [ -z "$PIDS" ]; then
                                          PIDS="$(pgrep -f "$PATTERN2" 2>/dev/null || true)"
                                        fi
                                        if [ -n "$PIDS" ]; then
                                          echo "Found simulation PIDs: $PIDS"
                                          for p in $PIDS; do
                                            killtree "$p"
                                          done
                                        else
                                          echo "No running process found by simulation pattern."
                                        fi
                                        if [ -f "$RWPID" ]; then
                                          WPID=$(cat "$RWPID" 2>/dev/null)
                                          [ -n "$WPID" ] && killtree "$WPID"
                                        fi
                                        if [ -f "$RPID" ]; then
                                          MPID=$(cat "$RPID" 2>/dev/null)
                                          [ -n "$MPID" ] && killtree "$MPID"
                                        fi
                                        rm -f "$RPID" "$RWPID"
                                        echo 0 > "$RLOCK"
                                        echo "=== Emergency Cleanup Finished ==="
                                    '
                                '''
                            }
                        }
                    } catch (Exception e) {
                        echo "POST(aborted) cleanup error: ${e.getMessage()}"
                    } finally {
                        env.GATLING_IS_RUNNING = "false"
                    }
                } else {
                    echo "POST(aborted): cleanup skipped (REMOTE_HOST or FINAL_TARGET empty)"
                }
            }
        }
    }
}