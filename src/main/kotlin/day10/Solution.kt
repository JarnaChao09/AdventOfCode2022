package day10

fun main() {
    fun List<Pair<Int, Int>>.findSignal(n: Int): Int = this.takeWhile { it.first < n }.lastOrNull()?.second ?: 1
    java.io.File("./input/day10/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .run {
            var currentCycle = 0
            var x = 1
            val values = mutableListOf<Pair<Int, Int>>()

            forEach {
                currentCycle += if (it == "noop") {
                    1
                } else {
                    x += it.split(" ")[1].toInt()
                    values.add((currentCycle + 2) to x)
                    2
                }
            }

            listOf(20, 60, 100, 140, 180, 220).sumOf {
                values.findSignal(it) * it
            }.also(::println)

            List(240) { i ->
                val curr = when (val mod = (i + 1) % 40) {
                    0 -> 40
                    else -> mod
                }
                val pos = values.findSignal(i + 1)
                println("$i $curr $pos")
                if (curr in pos..pos + 2) {
                    "#"
                } else {
                    "."
                }
            }.chunked(40).forEach { list ->
                list.forEach(::print)
                println()
            }
        }
}