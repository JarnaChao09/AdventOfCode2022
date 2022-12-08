package day8

fun main() {
    fun <T> List<List<T>>.transpose(): List<List<T>> {
        val ret = buildList {
            for (i in this@transpose.indices) {
                add(buildList {
                    for (j in this@transpose[0].indices) {
                        add(this@transpose[j][i])
                    }
                })
            }
        }
        return ret
    }

    fun List<Int>.checkIfVisible(index: Int): Boolean {
        if (index == 0 || index == this.size - 1) {
            return true
        }
        val curr = this[index]
        return this.take(index).map { it < curr }.reduce(Boolean::and) ||
                this.drop(index + 1).map { it < curr }.reduce(Boolean::and)
    }

    fun List<Int>.countVisibility(index: Int): Int {
        if (index == 0 || index == this.size - 1) {
            return 0
        }
        val curr = this[index]
        var fromLeft = 0
        var fromRight = 0
        for (i in this.take(index)) {
            fromLeft++
            if (i >= curr) {
                fromLeft = 1
            }
        }
        for (i in this.drop(index + 1).reversed()) {
            fromRight++
            if (i >= curr) {
                fromRight = 1
            }
        }
        return fromLeft * fromRight
    }
//    """
//        30373
//        25512
//        65332
//        33549
//        35390
//    """.trimIndent()
    java.io.File("./input/day8/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .map {
            it.toCharArray().toList().map { c -> c - '0' }
        }.toList().run {
            val t = this.transpose()
            val rows = buildList {
                for (row in this@run) {
                    add(List(row.size) { i ->
                        row.checkIfVisible(i) to row.countVisibility(i)
                    })
                }
            }.flatten()
            val cols = buildList {
                for (row in t) {
                    add(List(row.size) { i ->
                        row.checkIfVisible(i) to row.countVisibility(i)
                    })
                }
            }.transpose().flatten()

            rows.zip(cols).map { (l, r) ->
                (if (l.first || r.first) {
                    1
                } else {
                    0
                }) to (l.second * r.second)
            }.run {
                println(this.sumOf {
                    it.first
                })
                println(this.maxOf {
                    it.second
                })
            }
        }
}