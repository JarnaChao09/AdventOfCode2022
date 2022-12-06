package day6

fun main() {
    java.io.File("./input/day6/input.txt")
        .reader()
        .buffered()
        .readLine()
        .run {
            (windowedSequence(4) {
                it
            }.indexOfFirst {
                it.toSet().size == it.length
            } + 4).also(::println)

            (windowedSequence(14) {
                it
            }.indexOfFirst {
                it.toSet().size == it.length
            } + 14).also(::println)
        }
}