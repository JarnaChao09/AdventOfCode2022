package day2

//    1 A = X = R
//    2 B = Y = P
//    3 C = Z = S
//             T      L      W      W      T      L      L      W      T
//    listOf("A X", "B X", "C X", "A Y", "B Y", "C Y", "A Z", "B Z", "C Z",).asSequence()
//    listOf("A Y", "B X", "C Z").asSequence()

fun main() {
    java.io.File("./input/day2/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .map {
            it.split(" ")
        }.map {
            it.first()[0] - 'A' + 1 to it.last()[0] - 'X' + 1
        }.map {
            ((it.second - it.first + 4) % 3 to it.second) to (it.second - 1 to ((it.first + it.second) % 3) + 1)
        }.map { (p1, p2) ->
            (3 * p1.first + p1.second) to (3 * p2.first + p2.second)
        }.fold(0 to 0) { acc, (p1, p2) ->
            (acc.first + p1) to (acc.second + p2)
        }.also(::println)
}
