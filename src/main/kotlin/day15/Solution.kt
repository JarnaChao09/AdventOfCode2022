package day15

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.system.exitProcess

fun main() {
    infix fun Pair<Int, Int>.taxicab(other: Pair<Int, Int>): Int =
        (this.first - other.first).absoluteValue + (this.second - other.second).absoluteValue

    fun Set<Triple<Int, Int, Int>>.valid(x: Int, y: Int): Boolean = this.all { (sx, sy, d) ->
        (x - sx).absoluteValue + (y - sy).absoluteValue > d
    }

    fun Boolean.toInt(): Int = if (this) 1 else 0

    var min = Int.MAX_VALUE
    var max = Int.MIN_VALUE
    var maxDist = Int.MIN_VALUE
    val coverage = mutableSetOf<Triple<Int, Int, Int>>()
    val beacons = mutableSetOf<Pair<Int, Int>>()
    java.io.File("./input/day15/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .map { s ->
            s.replace(" ", "").split(":").map {
                it.replace(Regex("[^-,\\d]"), "")
                    .split(",")
                    .map(String::toInt)
                    .run {
                        first() to last()
                    }
            }.run {
                first() to last()
            }
        }.forEach { (sensor, beacon) ->
            val dist = sensor taxicab beacon
            min = min(min, min(sensor.first, beacon.first))
            max = max(max, max(sensor.first, beacon.first))
            maxDist = max(maxDist, dist)
            beacons.add(beacon)
            coverage.add(Triple(sensor.first, sensor.second, dist))
        }
    ((min - maxDist) until (max + maxDist)).sumOf { x ->
        (!coverage.valid(x, 2000000) && (x to 2000000) !in beacons).toInt()
    }.also(::println)

    for ((sx, sy, d) in coverage) {
        for (dx in 0..d + 1) {
            val dy = (d + 1) - dx
            for ((dirX, dirY) in listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)) {
                val newX = sx + (dx * dirX)
                val newY = sy + (dy * dirY)
                if (newX !in 0..4000000 || newY !in 0..4000000) {
                    continue
                }
                if (coverage.valid(newX, newY)) {
                    println(newX.toLong() * 4000000 + newY.toLong())
                    exitProcess(0)
                }
            }
        }
    }
}