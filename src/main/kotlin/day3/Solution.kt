package day3

fun main() {
    java.io.File("./input/day3/input.txt")
        .run {
            reader().buffered().lineSequence().map {
                (it.take(it.length / 2).toSet() intersect it.drop(it.length / 2).toSet()).first()
            }.sumOf {
                if (it in 'a'..'z') {
                    it - 'a' + 1
                } else {
                    it - 'A' + 27
                }
            }.also(::println)

            reader().buffered().lineSequence().chunked(3).map {
                it.map(String::toSet).reduce(Set<Char>::intersect).first()
            }.sumOf {
                if (it in 'a'..'z') {
                    it - 'a' + 1
                } else {
                    it - 'A' + 27
                }
            }.also(::println)
        }
}