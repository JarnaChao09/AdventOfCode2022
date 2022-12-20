package day20

data class Node(val value: Long, var prev: Node? = null, var next: Node? = null) {
    override fun toString(): String = "($value, ${prev!!.value}, ${next!!.value})"
}

fun List<Node>.mix(times: Int) = repeat(times) {
    this.forEach {
        it.prev!!.next = it.next
        it.next!!.prev = it.prev
        var (v, a, b) = it
        val move = v.mod(this.size - 1)
        repeat(move) {
            a = a!!.next
            b = b!!.next
        }
        a!!.next = it
        it.prev = a
        b!!.prev = it
        it.next = b
    }
}

fun List<Node>.solve(): Long = this.find {
    it.value == 0L
}!!.let {
    var r = 0L
    var y = it
    repeat(3) {
        repeat(1000) {
            y = y.next!!
        }
        r += y.value
    }
    r
}

fun List<Node>.fill() {
    this.zipWithNext { a, b ->
        a.next = b
        b.prev = a
    }

    this.last().next = this.first()
    this.first().prev = this.last()
}

fun <T1, T2, R> compose(f: (T2) -> R, g: (T1) -> T2): (T1) -> R = {
    f(g(it))
}

fun main() {
    java.io.File("./input/day20/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .map(compose(::Node, String::toLong))
        .toList()
        .run {
            val part1 = this.toList()
            val part2 = this.map {
                it.copy(value = it.value * 811589153)
            }

            part1.fill()
            part2.fill()

            part1.mix(1)
            part2.mix(10)

            println(part1.solve())
            println(part2.solve())
        }
}