package day4

fun main() {
    java.io.File("./input/day4/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .map { s ->
            s.split(",")
                .flatMap {
                    it.split("-").map(String::toInt)
                }
        }.map {
            val (o1, o2, o3, o4) = it
            val (s1, _, _, s4) = it.sorted()
            when {
                o1 == s1 && o2 == s4 -> 1
                o3 == s1 && o4 == s4 -> 1
                else -> 0
            } to when {
                o2 < o3 -> 0
                o4 < o1 -> 0
                else -> 1
            }
        }.fold(0 to 0) { acc, p ->
            (acc.first + p.first) to (acc.second + p.second)
        }.also(::println)
}