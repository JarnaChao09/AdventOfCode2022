package day13

sealed interface Data

object Empty : Data

data class Num(val num: Int) : Data

data class Vec(val vec: MutableList<Data>) : Data

fun main() {
    fun String.parse(): Data {
        var ret: Data = Empty
        val curr = ArrayDeque<Vec>()
        var currInt = ""
        for (c in this) {
            when (c) {
                '[' -> {
                    when (ret) {
                        is Empty -> {
                            ret = Vec(mutableListOf())
                            curr.addFirst(ret)
                        }

                        is Vec -> {
                            curr.addFirst(Vec(mutableListOf()))
                        }

                        is Num -> throw Exception("ret should never be Num")
                    }
                }

                ']' -> {
                    if (currInt.isNotEmpty()) {
                        curr.first().vec.add(Num(currInt.toInt()))
                        currInt = ""
                    }

                    val first = curr.removeFirst()

                    if (curr.isNotEmpty()) {
                        curr.first().vec.add(first)
                    }
                }

                ',' -> {
                    if (currInt.isNotEmpty()) {
                        curr.first().vec.add(Num(currInt.toInt()))
                        currInt = ""
                    }
                    continue
                }

                else -> {
                    currInt += c
                }
            }
        }
        return ret
    }

    operator fun Data.compareTo(other: Data): Int {
        return when (this) {
            Empty -> throw Exception("data should never be empty")
            is Num -> {
                when (other) {
                    Empty -> throw Exception("data should never be empty")
                    is Num -> this.num - other.num
                    is Vec -> Vec(mutableListOf(this)).compareTo(other)
                }
            }

            is Vec -> {
                when (other) {
                    Empty -> throw Exception("data should never be empty")
                    is Num -> this.compareTo(Vec(mutableListOf(other)))
                    is Vec -> {
                        for (i in 0 until kotlin.math.min(this.vec.size, other.vec.size)) {
                            val res = this.vec[i].compareTo(other.vec[i])
                            if (res != 0) {
                                return res
                            }
                        }
                        return this.vec.size - other.vec.size
                    }
                }
            }
        }
    }

    fun Boolean.toInt(): Int = if (this) 1 else 0

    java.io.File("./input/day13/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .fold(mutableListOf(mutableListOf<Data>())) { acc, s ->
            acc.apply {
                if (s.isNotEmpty()) {
                    this.last().add(s.parse())
                } else {
                    this.add(mutableListOf())
                }
            }
        }.also {
            it.mapIndexed { i, (l, r) -> (i + 1) * (l < r).toInt() }.sum().also(::println)
        }.flatten().toMutableList().also {
            val dividers = listOf("[[2]]", "[[6]]").map(String::parse).apply(it::addAll)
            it.sortedWith(Data::compareTo)
                .mapIndexed { i, d -> i * (d in dividers).toInt() + 1 }
                .reduce(Int::times)
                .also(::println)
        }
}