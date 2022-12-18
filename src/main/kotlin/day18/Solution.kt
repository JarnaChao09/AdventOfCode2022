package day18

fun main() {
    fun Boolean.toInt(): Int = if (this) 1 else 0

    """
        2,2,2
        1,2,2
        3,2,2
        2,1,2
        2,3,2
        2,2,1
        2,2,3
        2,2,4
        2,2,6
        1,2,5
        3,2,5
        2,1,5
        2,3,5
    """.trimIndent()
    java.io.File("./input/day18/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .map {
            it.split(",").map(String::toInt).let { (a, b, c) ->
                Triple(a, b, c)
            }
        }.toSet().run {
            val directions = listOf(
                Triple(-1, 0, 0),
                Triple(1, 0, 0),
                Triple(0, -1, 0),
                Triple(0, 1, 0),
                Triple(0, 0, -1),
                Triple(0, 0, 1),
            )

            val minX = this.minBy(Triple<Int, *, *>::first).first - 1
            val maxX = this.maxBy(Triple<Int, *, *>::first).first + 2
            val minY = this.minBy(Triple<*, Int, *>::second).second - 1
            val maxY = this.maxBy(Triple<*, Int, *>::second).second + 2
            val minZ = this.minBy(Triple<*, *, Int>::third).third - 1
            val maxZ = this.maxBy(Triple<*, *, Int>::third).third + 2

            val water = mutableSetOf<Triple<Int, Int, Int>>()
            val toFill = ArrayDeque(listOf(Triple(minX, minY, minZ)))

            while (toFill.isNotEmpty()) {
                val w = toFill.removeFirst()
                if (w in water || w in this) {
                    continue
                }

                water += w

                directions.forEach { (dx, dy, dz) ->
                    val (wx, wy, wz) = w
                    val tx = wx + dx
                    val ty = wy + dy
                    val tz = wz + dz
                    if (tx in minX..maxX && ty in minY..maxY && tz in minZ..maxZ) {
                        toFill += Triple(tx, ty, tz)
                    }
                }
            }

            this.fold(0 to 0) { acc, (x, y, z) ->
                var (old1, old2) = acc
                directions.forEach { (dx, dy, dz) ->
                    val check = Triple(x + dx, y + dy, z + dz)
                    old1 += (check !in this).toInt()
                    old2 += (check in water).toInt()
                }
                old1 to old2
            }.also(::println)
        }
}