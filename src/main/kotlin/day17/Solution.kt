package day17

import kotlin.math.max

fun main() {
    fun List<Pair<Int, Int>>.moveLeft(): List<Pair<Int, Int>> = this.map { (x, y) ->
        x - 1 to y
    }

    fun List<Pair<Int, Int>>.moveRight(): List<Pair<Int, Int>> = this.map { (x, y) ->
        x + 1 to y
    }

    fun List<Pair<Int, Int>>.isValid(occupied: Set<Pair<Int, Int>>): Boolean {
        for (coords in this) {
            if (coords in occupied || coords.first < 1 || coords.first > 7 || coords.second < 1) {
                return false
            }
        }
        return true
    }

    fun List<Pair<Long, Long>>.isValid(occupied: Set<Pair<Long, Long>>): Boolean {
        for (coords in this) {
            if (coords in occupied || coords.first < 1 || coords.first > 7 || coords.second < 1) {
                return false
            }
        }
        return true
    }

    fun MutableSet<Pair<Int, Int>>.run1(numRocks: Int, input: List<Char>, shapes: List<List<Pair<Int, Int>>>): Int {
        val occupied = this
        var top = 0
        var i = 0
        for (c in 1..numRocks) {
            var shape = shapes[(c - 1) % shapes.size].map { (x, y) ->
                x + 2 to y + top + 3
            }

            while (true) {
                var next = when (input[i]) {
                    '<' -> {
                        shape.moveLeft()
                    }

                    '>' -> {
                        shape.moveRight()
                    }

                    else -> error("unreachable")
                }

                i = (i + 1) % input.size

                if (next.isValid(occupied)) {
                    shape = next
                }

                next = shape.map { (x, y) ->
                    x to y - 1
                }

                if (next.isValid(occupied)) {
                    shape = next
                } else {
                    top = max(top, shape.maxOf {
                        occupied += it
                        it.second
                    })
                    break
                }
            }
        }
        return top
    }

    fun MutableSet<Pair<Long, Long>>.run2(numRocks: Int, input: List<Char>, shapes: List<List<Pair<Int, Int>>>): Long {
        val occupied = this
        var top = 0L
        var i = 0
        val delta = mutableListOf<Long>()
        for (c in 1..numRocks) {
            var shape = shapes[(c - 1) % shapes.size].map { (x, y) ->
                (x + 2).toLong() to (y + top + 3)
            }

            while (true) {
                var next = when (input[i]) {
                    '<' -> {
                        shape.map { (x, y) ->
                            x - 1 to y
                        }
                    }

                    '>' -> {
                        shape.map { (x, y) ->
                            x + 1 to y
                        }
                    }

                    else -> error("unreachable")
                }

                i = (i + 1) % input.size

                if (next.isValid(occupied)) {
                    shape = next
                }

                next = shape.map { (x, y) ->
                    x to y - 1
                }

                if (next.isValid(occupied)) {
                    shape = next
                } else {
                    var tmp = top
                    tmp = max(tmp, shape.maxOf {
                        occupied += it
                        it.second
                    })
                    delta.add(tmp - top)
                    top = tmp
                    break
                }
            }
        }

        var l = 0
        for (len in 100..5000) {
            if (delta.subList(delta.size - len, delta.size) == delta.subList(delta.size - 2 * len, delta.size - len)) {
                l = len
                break
            }
        }

        val sum = delta.subList(delta.size - l, delta.size).sum()
        val rem = 1000000000000L - numRocks
        val n = rem / l
        var ret = top + sum * n
        val m = (rem % l).toInt()

        ret += delta.subList(delta.size - l, delta.size - l + m).sum()

        return ret
    }
    java.io.File("./input/day17/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .flatMap {
            it.toCharArray().toList()
        }.toList().run {
            val shapes = listOf(
                listOf(1 to 1, 2 to 1, 3 to 1, 4 to 1),         // -
                listOf(1 to 2, 2 to 3, 2 to 2, 2 to 1, 3 to 2), // +
                listOf(1 to 1, 2 to 1, 3 to 1, 3 to 2, 3 to 3), // L
                listOf(1 to 1, 1 to 2, 1 to 3, 1 to 4),         // |
                listOf(1 to 1, 2 to 1, 1 to 2, 2 to 2),         // square
            )
            mutableSetOf<Pair<Int, Int>>().run1(2022, this, shapes).also(::println)
            mutableSetOf<Pair<Long, Long>>().run2(100000, this, shapes).also(::println)
        }
}