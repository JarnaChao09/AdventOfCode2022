package day19

import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        java.io.File("./input/day19/input.txt")
            .reader()
            .buffered()
            .lineSequence()
            .mapIndexed { i, it ->
                val blueprint = it.substringAfter(":")
                    .trim()
                    .split(".")
                    .take(4)
                    .map { s ->
                        s.trim().replace(Regex("\\D"), " ").split(" ").mapNotNull { i ->
                            i.toIntOrNull()
                        }
                    }
                (i + 1) * search(blueprint, 24) to (if (i < 3) search(blueprint, 32) else 1)
            }.fold(0 to 1) { acc, p ->
                (acc.first + p.first) to (acc.second * p.second)
            }.also(::println)
    }.also {
        println("time taken: $it ms")
    }
}

fun search(
    blueprint: List<List<Int>>,
    timeLeft: Int,
    ores: List<Int> = listOf(0, 0, 0, 0),
    machines: List<Int> = listOf(1, 0, 0, 0),
    floor: Int = 0
): Int {
    if (timeLeft == 0) {
        return ores.last()
    }

    // current geodes + current generation of geodes + if another geode machine was added every minute
    val bestPossible = ores.last() + timeLeft * machines.last() + (timeLeft * (timeLeft - 1) / 2)

    if (bestPossible < floor) {
        return 0
    }

    // ores/blueprint/machine = [o, c, b, g]

    // true if more ore robots are needed
    val needed = machines.first() < maxOf(blueprint[1][0], blueprint[2][0], blueprint[3][0])
    val ore = ores[0] >= blueprint[0][0] && needed
    val clay = ores[0] >= blueprint[1][0]
    val obsidian = ores[0] >= blueprint[2][0] && ores[1] >= blueprint[2][1]
    val geode = ores[0] >= blueprint[3][0] && ores[2] >= blueprint[3][1]

    val newOres = List(ores.size) {
        ores[it] + machines[it]
    }

    var currentBest = floor
    if (geode) {
        val (o, c, b, g) = newOres
        val (om, cm, bm, gm) = machines
        val testOres = listOf(o - blueprint[3][0], c, b - blueprint[3][1], g)
        val testMachines = listOf(om, cm, bm, gm + 1)
        currentBest = max(
            currentBest,
            search(blueprint, timeLeft - 1, ores = testOres, machines = testMachines, floor = currentBest)
        )
    }
    if (obsidian) {
        val (o, c, b, g) = newOres
        val (om, cm, bm, gm) = machines
        val testOres = listOf(o - blueprint[2][0], c - blueprint[2][1], b, g)
        val testMachines = listOf(om, cm, bm + 1, gm)
        currentBest = max(
            currentBest,
            search(blueprint, timeLeft - 1, ores = testOres, machines = testMachines, floor = currentBest)
        )
    }
    if (clay) {
        val (o, c, b, g) = newOres
        val (om, cm, bm, gm) = machines
        val testOres = listOf(o - blueprint[1][0], c, b, g)
        val testMachines = listOf(om, cm + 1, bm, gm)
        currentBest = max(
            currentBest,
            search(blueprint, timeLeft - 1, ores = testOres, machines = testMachines, floor = currentBest)
        )
    }
    if (ore) {
        val (o, c, b, g) = newOres
        val (om, cm, bm, gm) = machines
        val testOres = listOf(o - blueprint[0][0], c, b, g)
        val testMachines = listOf(om + 1, cm, bm, gm)
        currentBest = max(
            currentBest,
            search(blueprint, timeLeft - 1, ores = testOres, machines = testMachines, floor = currentBest)
        )
    }

    currentBest =
        max(currentBest, search(blueprint, timeLeft - 1, ores = newOres, machines = machines, floor = currentBest))
    return currentBest
}