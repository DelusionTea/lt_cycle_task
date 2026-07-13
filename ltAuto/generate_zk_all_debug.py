#!/usr/bin/env python3
"""Сгенерировать simulations/ZK/All/ZK_all_debug.java по всем *Scenario.java домена ZK.

Запуск: python3 ltAuto/generate_zk_all_debug.py
"""

from __future__ import annotations

import glob
import os

_REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SCENARIOS_DIR = os.path.join(_REPO, "gatling", "src", "test", "java", "scenarios", "ZK")
OUT_PATH = os.path.join(
    _REPO, "gatling", "src", "test", "java", "simulations", "ZK", "All", "ZK_all_debug.java"
)
USERS = 1


def main():
    names = sorted(
        os.path.basename(p).replace("Scenario.java", "")
        for p in glob.glob(os.path.join(SCENARIOS_DIR, "*Scenario.java"))
    )
    if not names:
        raise SystemExit(f"Сценарии не найдены: {SCENARIOS_DIR}")

    blocks = []
    for name in names:
        blocks.append(
            f"                {name}Scenario.Debug\n"
            f"                        .injectOpen(atOnceUsers({USERS}))\n"
            f"                        .protocols(ZKProtocol.httpProtocol)"
        )

    body = ",\n\n".join(blocks)
    java = f"""package simulations.ZK.All;

import feeders.ZK.ZKProtocol;

import io.gatling.javaapi.core.Simulation;

import scenarios.ZK.*;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;

/**
 * Debug-симуляция ZK: последовательный прогон всех UC каждого контроллера.
 * Аналог resources/OTT_all_debug.java для домена ZK.
 *
 * Запуск (из gatling/):
 *   mvn gatling:test -Dgatling.simulationClass=simulations.ZK.All.ZK_all_debug \\
 *       -DzkBaseUrl=https://host:port
 */
public class ZK_all_debug extends Simulation {{

    {{
        setUp(
{body}
        );
    }}
}}
"""
    os.makedirs(os.path.dirname(OUT_PATH), exist_ok=True)
    with open(OUT_PATH, "w", encoding="utf-8") as fh:
        fh.write(java)
    print(f"Generated {OUT_PATH} ({len(names)} Debug-сценариев, {USERS} user each)")


if __name__ == "__main__":
    main()
