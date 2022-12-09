package day9

import kotlin.math.hypot
import kotlin.math.sign

fun main() {
    java.io.File("./input/day9/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .flatMap {
            val (dirS, distS) = it.split(" ")
            val dir = when (dirS) {
                "L" -> -1 to 0
                "R" -> 1 to 0
                "U" -> 0 to 1
                "D" -> 0 to -1
                else -> throw Exception("uhhh")
            }
            val dist = distS.toInt()

            List(dist) {
                dir
            }
        }.toList().run {
            var head = 0 to 0
            var tail = 0 to 0
            val cords = mutableSetOf<Pair<Int, Int>>()

            for (cord in this) {
                val oldHead = head
                head = (head.first + cord.first) to (head.second + cord.second)
                val diff = hypot((head.first - tail.first).toDouble(), (head.second - tail.second).toDouble())
                if (diff >= 2.0) {
                    tail = oldHead
                }

                cords.add(tail)
            }

            println(cords.size)

            cords.clear()
            val rope = MutableList(10) {
                0 to 0
            }
            for (cord in this) {
                rope[0] = (rope[0].first + cord.first) to (rope[0].second + cord.second)
                for (i in 1 until 10) {
                    val first = rope[i - 1]
                    val second = rope[i]
                    val diff = (first.first - second.first) to (first.second - second.second)
                    if (hypot(diff.first.toDouble(), diff.second.toDouble()) >= 2.0) {
                        rope[i] = (rope[i].first + diff.first.sign) to (rope[i].second + diff.second.sign)
                    }
                }
                cords.add(rope[9])
            }

            println(cords.size)
        }
}