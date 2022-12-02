package day1

import java.io.File

fun main() {
    File("./input/day1/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .fold(mutableListOf(mutableListOf<Int>())) { acc, s ->
            acc.apply {
                if (s.isNotEmpty()) {
                    this.last().add(s.toInt())
                } else {
                    this.add(mutableListOf())
                }
            }
        }.run {
            println("Part 1: ${this.maxOfOrNull(Iterable<Int>::sum)}")
            println("Part 2: ${this.map(Iterable<Int>::sum).sorted().takeLast(3).sum()}")
        }
}