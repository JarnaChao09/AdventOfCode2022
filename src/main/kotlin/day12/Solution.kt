package day12

fun main() {
    fun List<List<Pair<Char, Int>>>.bfs(deque: ArrayDeque<Triple<Int, Int, Int>>): Int {
        var ret = 0
        val visited = mutableSetOf<Pair<Int, Int>>()
        while (deque.isNotEmpty()) {
            val (r, c, d) = deque.removeFirst()
            val coords = r to c

            if (coords !in visited) {
                visited.add(coords)
                if (this[r][c].first == 'E') {
                    ret = d
                    break
                }
                for ((dr, dc) in listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)) {
                    val newR = r + dr
                    val newC = c + dc
                    if (newR in this.indices && newC in this[0].indices && this[newR][newC].second <= this[r][c].second + 1) {
                        deque.add(Triple(newR, newC, d + 1))
                    }
                }
            }
        }
        return ret
    }
    java.io.File("./input/day12/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .map {
            it.map { c ->
                c to when (c) {
                    'S' -> 1
                    'E' -> 26
                    else -> c - 'a' + 1
                }
            }
        }.toList().run {
            val deque1 = ArrayDeque<Triple<Int, Int, Int>>()
            val deque2 = ArrayDeque<Triple<Int, Int, Int>>()
            this.forEachIndexed { i, it ->
                it.forEachIndexed { j, e ->
                    if (e.first == 'S') {
                        deque1.add(Triple(i, j, 0))
                    }
                    if (e.second == 1) {
                        deque2.add(Triple(i, j, 0))
                    }
                }
            }

            this.bfs(deque1).also(::println)
            this.bfs(deque2).also(::println)
        }
}