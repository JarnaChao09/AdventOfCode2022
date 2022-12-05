package day5

fun main() {
    val reader = java.io.File("./input/day5/input.txt").bufferedReader()

    val stacks0 = mutableListOf<ArrayDeque<String>>()
    val stacks1 = mutableListOf<ArrayDeque<String>>()

    var line = reader.readLine()
    while (line.isNotEmpty()) {
        if (line.contains(Regex("\\d"))) {
            break
        }
        for ((i, c) in line.replace("    ", " ").split(" ").withIndex()) {
            if (i == stacks0.size) {
                stacks0.add(ArrayDeque())
                stacks1.add(ArrayDeque())
            }
            if (c.isNotEmpty()) {
                stacks0[i].addLast(c)
                stacks1[i].addLast(c)
            }
        }
        line = reader.readLine()
    }

    reader.run {
        readLine()
        readLines().map {
            it.replace(Regex("[a-z]"), "").split(Regex("( )+")).drop(1)
        }.forEach {
            val (amt, from, to) = it.map(String::toInt)
            val tmp = mutableListOf<String>()
            repeat(amt) {
                tmp.add(stacks1[from - 1].removeFirst())
                stacks0[to - 1].addFirst(stacks0[from - 1].removeFirst())
            }
            tmp.reversed().forEach { s ->
                stacks1[to - 1].addFirst(s)
            }
        }
    }

    stacks0.forEach {
        print(it.first().replace("[", "").replace("]", ""))
    }
    println()
    stacks1.forEach {
        print(it.first().replace("[", "").replace("]", ""))
    }
}