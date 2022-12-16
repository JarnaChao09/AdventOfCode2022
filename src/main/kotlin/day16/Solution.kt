package day16

typealias ValveMap = Map<String, Pair<Int, List<String>>>
typealias MutableValveMap = MutableMap<String, Pair<Int, List<String>>>

fun main() {
    fun Boolean.toInt(): Int = if (this) 1 else 0

    val graph: MutableValveMap = mutableMapOf()
    """
        Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        Valve BB has flow rate=13; tunnels lead to valves CC, AA
        Valve CC has flow rate=2; tunnels lead to valves DD, BB
        Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
        Valve EE has flow rate=3; tunnels lead to valves FF, DD
        Valve FF has flow rate=0; tunnels lead to valves EE, GG
        Valve GG has flow rate=0; tunnels lead to valves FF, HH
        Valve HH has flow rate=22; tunnel leads to valve GG
        Valve II has flow rate=0; tunnels lead to valves AA, JJ
        Valve JJ has flow rate=21; tunnel leads to valve II
    """.trimIndent()
    java.io.File("./input/day16/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .forEach { s ->
            val (l, r) = s.replace(",", "").split(";").map {
                it.trim().split(" ")
            }
            val rate = l.last().replace(Regex("\\D"), "").toInt()
            val edges = r.drop(4)
            graph[l[1]] = rate to edges
        }

    println(part1(1, "AA", 0L, 0L, setOf(), graph, mutableMapOf()))
    println(part2(1, "AA", "AA", 0L, 0L, setOf(), graph, mutableMapOf()))
}

fun part1(
    time: Int,
    currentLocation: String,
    rate: Long,
    currentPressure: Long,
    opened: Set<String>,
    valves: ValveMap,
    cache: MutableMap<Triple<Int, String, Long>, Long>
): Long? {
    if (time > 30) {
        return currentPressure
    }

    val cacheKey = Triple(time, currentLocation, rate)
    val cachedValue = cache[cacheKey] ?: -1L

    if (cachedValue >= currentPressure) {
        return null
    }
    cache[cacheKey] = currentPressure

    val currentValue = valves[currentLocation]!!

    val bestResultOpenCurrent = if (currentValue.first > 0 && currentLocation !in opened) {
        val newOpened = opened.toMutableSet()
        newOpened += currentLocation

        val newPressure = currentPressure + rate
        val newRate = rate + currentValue.first
        part1(time + 1, currentLocation, newRate, newPressure, newOpened, valves, cache)
    } else {
        null
    }

    val bestResultDownTunnels = currentValue
        .second
        .mapNotNull {
            part1(time + 1, it, rate, currentPressure + rate, opened, valves, cache)
        }
        .maxOrNull()

    return max(bestResultDownTunnels, bestResultOpenCurrent)
}

fun part2(
    time: Int,
    myLocation: String,
    elephantLocation: String,
    rate: Long,
    currentPressure: Long,
    opened: Set<String>,
    valves: ValveMap,
    cache: MutableMap<Triple<Int, Pair<String, String>, Long>, Long>
): Long? {
    if (time > 26) {
        return currentPressure
    }

    val cacheKey = Triple(time, myLocation to elephantLocation, rate)
    val cachedValue = cache[cacheKey] ?: -1L

    if (cachedValue >= currentPressure) {
        return null
    }
    cache[cacheKey] = currentPressure

    val (myRate, myTunnels) = valves[myLocation]!!
    val (elephantRate, elephantTunnels) = valves[elephantLocation]!!

    val myOpen = myRate > 0 && myLocation !in opened
    val elephantOpen = elephantRate > 0 && elephantLocation !in opened

    val ret = mutableListOf<Long?>()

    if (myOpen) {
        // open my valve, elephant moves
        val newOpen = opened.toMutableSet()
        newOpen += myLocation

        for (newElephantLocation in elephantTunnels) {
            ret.add(
                part2(
                    time + 1,
                    myLocation,
                    newElephantLocation,
                    rate + myRate,
                    currentPressure + rate,
                    newOpen,
                    valves,
                    cache
                )
            )
        }
    }

    if (elephantOpen) {
        // open elephant valve, i move
        val newOpen = opened.toMutableSet()
        newOpen += elephantLocation

        for (newMyLocation in myTunnels) {
            ret.add(
                part2(
                    time + 1,
                    newMyLocation,
                    elephantLocation,
                    rate + elephantRate,
                    currentPressure + rate,
                    newOpen,
                    valves,
                    cache
                )
            )
        }
    }

    if (myOpen && elephantOpen && myLocation != elephantLocation) {
        // elephant and i open our valves
        val newOpen = opened.toMutableSet()
        newOpen += elephantLocation
        newOpen += myLocation

        ret.add(
            part2(
                time + 1,
                myLocation ,
                elephantLocation,
                rate + myRate + elephantRate,
                currentPressure + rate,
                newOpen,
                valves,
                cache
            )
        )
    }

    // both elephant and i move
    for (newElephantLocation in elephantTunnels) {
        for (newMyLocation in myTunnels) {
            ret.add(
                part2(
                    time + 1,
                    newMyLocation,
                    newElephantLocation,
                    rate,
                    currentPressure + rate,
                    opened,
                    valves,
                    cache
                )
            )
        }
    }

    return ret.filterNotNull().maxOrNull()
}

fun max(a: Long?, b: Long?): Long? = when {
    a == null && b == null -> null
    a == null -> b
    b == null -> a
    else -> kotlin.math.max(a, b)
}