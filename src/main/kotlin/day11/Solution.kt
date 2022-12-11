package day11

data class Monkey(
    val items: MutableList<Long>,
    val op: (Long) -> Long,
    val test: Long,
    val trueIndex: Int,
    val falseIndex: Int,
    var count: Long = 0
)

infix fun <T1, T2, R> ((T2) -> R).compose(other: (T1) -> T2): (T1) -> R = { this(other(it)) }

fun createOp(op: String, other: String): (Long) -> Long = when (other) {
    "old" -> {
        when (op) {
            "+" -> times2()
            "*" -> square()
            else -> throw Exception("uhhh")
        }
    }

    else -> {
        when (op) {
            "+" -> createAddOp(other.toLong())
            "*" -> createMultOp(other.toLong())
            else -> throw Exception("uhhh")
        }
    }
}

fun createAddOp(n: Long): (Long) -> Long = { it + n }

fun createMultOp(n: Long): (Long) -> Long = { it * n }

fun times2(): (Long) -> Long = { it + it }

fun square(): (Long) -> Long = { it * it }

fun List<Monkey>.run(times: Int, worry: Int, lcm: Long): Long {
    repeat(times) {
        this.forEach { monkey ->
            monkey.items.forEach { item ->
                val newWorry = (monkey.op(item) / worry) % lcm
                this[if (newWorry % monkey.test == 0L) monkey.trueIndex else monkey.falseIndex].items.add(newWorry)
            }
            monkey.count += monkey.items.size
            monkey.items.clear()
        }
    }
    return this.sortedBy(Monkey::count).takeLast(2).fold(1L) { acc, monkey -> acc * monkey.count }
}

fun main() {
    java.io.File("./input/day11/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .fold(mutableListOf<MutableList<String>>(mutableListOf())) { acc, s ->
            if (s.isNotEmpty()) {
                acc.last().add(s.trim())
            } else {
                acc.add(mutableListOf())
            }
            acc
        }.map { s ->
            val startingItems = mutableListOf<Long>()
            var op: (Long) -> Long = { it }
            var test = 0L
            var ti = 0
            var fi = 0
            s.filter {
                !it.startsWith("Monkey")
            }.map {
                val l = it.split(":").map(String::trim)
                when (l.first()) {
                    "Starting items" -> {
                        val toNum: (String) -> Long = String::toLong
                        startingItems.addAll(l[1].split(", ").map(toNum compose String::trim))
                    }

                    "Operation" -> {
                        val (_, _, _, opString, value) = l[1].split(" ")
                        op = createOp(opString, value)
                    }

                    "Test" -> {
                        test = l[1].split(" ").last().toLong()
                    }

                    "If true" -> {
                        ti = l[1].split(" ").last().toInt()
                    }

                    "If false" -> {
                        fi = l[1].split(" ").last().toInt()
                    }

                    else -> {
                        throw Exception("uhhh")
                    }
                }
            }
            Monkey(
                items = startingItems,
                op = op,
                test = test,
                trueIndex = ti,
                falseIndex = fi,
            )
        }.run {
            val lcm = this.map(Monkey::test).fold(1L, Long::times)
            List(this.size) {
                this[it].copy(items = MutableList(this[it].items.size) { i ->
                    this[it].items[i]
                }) // create new list, since copy will use the same memory reference
            }.run(20, 3, lcm).also(::println)

            List(this.size) {
                this[it].copy(items = MutableList(this[it].items.size) { i ->
                    this[it].items[i]
                }) // create new list, since copy will use the same memory reference
            }.run(10000, 1, lcm).also(::println)
        }
}