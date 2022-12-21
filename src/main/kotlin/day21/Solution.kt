package day21

sealed interface Fun {
    operator fun plus(other: Fun): Fun = Add(this, other)

    operator fun minus(other: Fun): Fun = Sub(this, other)

    operator fun times(other: Fun): Fun = Mul(this, other)

    operator fun div(other: Fun): Fun = Div(this, other)

    fun fullEval(env: Map<String, Fun>): Long

    fun partialEval(env: Map<String, Fun>): Fun

    operator fun contains(s: String): Boolean
}

data class Const(val value: Long) : Fun {
    override fun toString(): String = value.toString()

    override fun fullEval(env: Map<String, Fun>): Long = value

    override fun partialEval(env: Map<String, Fun>): Fun = this

    override fun contains(s: String): Boolean = this.toString() == s
}

data class Var(val name: String) : Fun {
    override fun toString(): String = name

    override fun fullEval(env: Map<String, Fun>): Long {
        for ((i, j) in env) {
            if (i == name) {
                return j.fullEval(env)
            }
        }
        error("no value given for $name in env")
    }

    override fun partialEval(env: Map<String, Fun>): Fun {
        for ((i, j) in env) {
            if (i == name) {
                return j.partialEval(env)
            }
        }
        return this
    }

    override fun contains(s: String): Boolean = this.name == s
}

data class Add(val left: Fun, val right: Fun) : Fun {
    override fun toString(): String = "($left + $right)"

    override fun fullEval(env: Map<String, Fun>): Long = left.fullEval(env) + right.fullEval(env)

    override fun partialEval(env: Map<String, Fun>): Fun = Add(left.partialEval(env), right.partialEval(env))

    override fun contains(s: String): Boolean = left.contains(s) || right.contains(s)
}

data class Sub(val left: Fun, val right: Fun) : Fun {
    override fun toString(): String = "($left - $right)"

    override fun fullEval(env: Map<String, Fun>): Long = left.fullEval(env) - right.fullEval(env)

    override fun partialEval(env: Map<String, Fun>): Fun = Sub(left.partialEval(env), right.partialEval(env))

    override fun contains(s: String): Boolean = left.contains(s) || right.contains(s)
}

data class Mul(val left: Fun, val right: Fun) : Fun {
    override fun toString(): String = "($left * $right)"

    override fun fullEval(env: Map<String, Fun>): Long = left.fullEval(env) * right.fullEval(env)

    override fun partialEval(env: Map<String, Fun>): Fun = Mul(left.partialEval(env), right.partialEval(env))

    override fun contains(s: String): Boolean = left.contains(s) || right.contains(s)
}

data class Div(val left: Fun, val right: Fun) : Fun {
    override fun toString(): String = "($left / $right)"

    override fun fullEval(env: Map<String, Fun>): Long = left.fullEval(env) / right.fullEval(env)

    override fun partialEval(env: Map<String, Fun>): Fun = Div(left.partialEval(env), right.partialEval(env))

    override fun contains(s: String): Boolean = left.contains(s) || right.contains(s)
}

fun createMonkey(input: String): Fun {
    val tmp = input.split(" ")
    return if (tmp.size == 1) {
        Const(tmp.first().toLong())
    } else {
        val (l, op, r) = tmp
        when (op) {
            "+" -> Add(Var(l), Var(r))
            "-" -> Sub(Var(l), Var(r))
            "*" -> Mul(Var(l), Var(r))
            "/" -> Div(Var(l), Var(r))
            else -> error("unreachable")
        }
    }
}

fun inverse(root: Fun, value: Long): Long {
    return when (root) {
        is Add -> {
            if ("humn" in root.left) {
                inverse(root.left, value - root.right.fullEval(emptyMap()))
            } else {
                inverse(root.right, value - root.left.fullEval(emptyMap()))
            }
        }

        is Sub -> {
            if ("humn" in root.left) {
                inverse(root.left, value + root.right.fullEval(emptyMap()))
            } else {
                inverse(root.right, root.left.fullEval(emptyMap()) - value)
            }
        }

        is Mul -> {
            if ("humn" in root.left) {
                inverse(root.left, value / root.right.fullEval(emptyMap()))
            } else {
                inverse(root.right, value / root.left.fullEval(emptyMap()))
            }
        }

        is Div -> {
            if ("humn" in root.left) {
                inverse(root.left, value * root.right.fullEval(emptyMap()))
            } else {
                inverse(root.right, root.left.fullEval(emptyMap()) / value)
            }
        }

        is Var -> {
            value
        }

        is Const -> error("unreachable")
    }
}

fun Map<String, Fun>.part2(root: Fun) {
    val (left, right) = when (root) {
        is Add -> {
            root.left to root.right
        }

        is Sub -> {
            root.left to root.right
        }

        is Mul -> {
            root.left to root.right
        }

        is Div -> {
            root.left to root.right
        }

        else -> error("unreachable")
    }

    val partialLeft = left.partialEval(this)
    val partialRight = left.partialEval(this)

    if ("humn" in partialLeft) {
        inverse(partialLeft, right.fullEval(this)).also(::println)
    } else {
        inverse(partialRight, left.fullEval(this)).also(::println)
    }
}

fun main() {
    val env = java.io.File("./input/day21/input.txt")
        .reader()
        .buffered()
        .lineSequence()
        .map {
            val (name, operation) = it.split(":").map(String::trim)
            name to createMonkey(operation)
        }.toMap()

    val root = env["root"]!!
    println(root.fullEval(env))

    env.filterKeys {
        it != "humn"
    }.part2(root)
}