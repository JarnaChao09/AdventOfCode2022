package day25

import java.math.BigInteger

fun main() {
    java.io.File("./input/day25/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .map {
            it
                .reversed()
                .toList()
                .mapIndexed { i, c ->
                    when (c) {
                        '-' -> -1
                        '=' -> -2
                        else -> c - '0'
                    }.let { v ->
                        v.toBigInteger() * (0 until i).fold(1.toBigInteger()) { acc, _ -> acc * 5.toBigInteger() }
                    }
                }.reduce(BigInteger::plus)
        }.reduce(BigInteger::plus).let {
            var output = ""
            var total = it
            while (total > 0.toBigInteger()) {
                val rem = total % 5.toBigInteger()
                total /= 5.toBigInteger()

                if (rem <= 2.toBigInteger()) {
                    output = "$rem$output"
                } else {
                    output = "${"=-"[rem.toInt() - 3]}$output"
                    total++
                }
            }
            output
        }.also(::println)
}