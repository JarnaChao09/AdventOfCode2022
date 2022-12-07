package day7

fun main() {
    val dirs = mutableMapOf<String, MutableList<String>>()
    var curr = ""
    java.io.File("./input/day7/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .filter {
            it != "$ ls"
        }
        .forEach {
            if (it.startsWith("$ cd")) {
                when (val to = it.split(" ")[2]) {
                    ".." -> {
                        curr = curr.substring(0, curr.lastIndexOf("/"))
                    }
                    "/" -> {
                        curr = "root"
                    }
                    else -> {
                        curr += "/$to"
                    }
                }
            } else {
                val (first, second) = it.split(" ")
                when (first) {
                    "dir" -> {
                        dirs.getOrPut(curr) {
                            mutableListOf()
                        }.add("$curr/$second")
                    }
                    else -> {
                        dirs.getOrPut(curr) {
                            mutableListOf()
                        }.add(first)
                    }
                }
            }
        }

    val dirSums = mutableMapOf<String, Int>()

    for (k in dirs.keys.reversed()) {
        dirSums[k] = dirs[k]?.map {
            if (it in dirs) {
                dirSums[it]!!
            } else {
                it.toInt()
            }
        }!!.sum()
    }

    dirSums.values.filter {
        it <= 100000
    }.sum().also(::println)

    val unused = 70000000 - dirSums["root"]!!

    dirSums.values.filter {
        it + unused > 30000000
    }.min().also(::println)
}