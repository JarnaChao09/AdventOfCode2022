package day14

import kotlin.math.max
import kotlin.math.min

fun main() {
    var minX = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var minY = Int.MAX_VALUE
    var maxY = Int.MIN_VALUE

    val occupied = mutableSetOf<Pair<Int, Int>>()
    java.io.File("./input/day14/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .map { s ->
            s.split(" -> ").flatMap {
                it.split(",").map(String::toInt)
            }.chunked(2)
        }.forEach {
            for ((c1, c2) in it.zipWithNext()) {
                val (y1, x1) = c1
                val (y2, x2) = c2

                val lx = min(x1, x2)
                val ux = max(x1, x2)
                val ly = min(y1, y2)
                val uy = max(y1, y2)

                minX = min(minX, lx)
                maxX = max(maxX, ux)
                minY = min(minY, ly)
                maxY = max(maxY, uy)

                for (i in lx..ux) {
                    occupied.add(i to y1)
                }
                for (i in ly..uy) {
                    occupied.add(x1 to i)
                }
            }
        }

    val part1 = occupied.toMutableSet()
    val part2 = occupied.toMutableSet()

    var c = 0
    var done = false
    while (!done) {
        var curr = 0 to 500
        var atRest = false
        while (!atRest) {
            val cx = curr.first + 1
            val cy = curr.second
            if (cx > maxX) {
                done = true
                break
            }
            if (cx to cy !in part1) {
                curr = cx to cy
                continue
            }
            if (cx to (cy - 1) !in part1) {
                curr = cx to (cy - 1)
                continue
            }
            if (cx to (cy + 1) !in part1) {
                curr = cx to (cy + 1)
                continue
            }
            atRest = true
            part1 += curr
        }
        c += if (!done) 1 else 0
    }
    println(c)

    c = 0
    done = false
    while (!done) {
        var curr = 0 to 500
        var atRest = false
        while (!atRest) {
            if (curr in part2) {
                done = true
                break
            }
            val cx = curr.first + 1
            val cy = curr.second
            if (cx == maxX + 2) {
                part2 += curr
                if (cx to cy !in part2) {
                    part2 += cx to cy
                    break
                } else if (cx to (cy - 1) !in part2) {
                    part2 += cx to (cy - 1)
                    break
                } else if (cx to (cy + 1) !in part2) {
                    part2 += cx to (cy + 1)
                    break
                }
            } else if (cx to cy !in part2) {
                curr = cx to cy
                continue
            } else if (cx to (cy - 1) !in part2) {
                curr = cx to (cy - 1)
                continue
            } else if (cx to (cy + 1) !in part2) {
                curr = cx to (cy + 1)
                continue
            }
            atRest = true
            part2 += curr
        }
        c += if (!done) 1 else 0
    }
    println(c)
}