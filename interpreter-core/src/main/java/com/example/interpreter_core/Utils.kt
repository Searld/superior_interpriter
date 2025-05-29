package com.example.interpreter_core

import java.util.Stack

object Utils {
    fun tokenize(expr: String): List<String> {
        val spaced = expr
            .replace("(", " ( ")
            .replace(")", " ) ")
            .replace("+", " + ")
            .replace("-", " - ")
            .replace("*", " * ")
            .replace("/", " / ")
            .replace("%", " % ")
            .trim()
        val parts = spaced.split("\\s+".toRegex())
        return mergeArrayTokens(parts)
    }


    private fun mergeArrayTokens(tokens: List<String>): List<String> {
        val out = mutableListOf<String>()
        var i = 0
        while (i < tokens.size) {
            val t = tokens[i]
            if (t.contains('[') && !t.endsWith("]")) {
                val sb = StringBuilder(t)
                i++
                while (i < tokens.size) {
                    sb.append(tokens[i])
                    if (tokens[i].endsWith("]")) break
                    i++
                }
                out += sb.toString()
            } else {
                out += t
            }
            i++
        }
        return out
    }

    private val arrayRegex = Regex("""^\w+\[.+]$""")

    fun toRPN(tokens: List<String>, env: Map<String, Int>): List<String> {
        val out = mutableListOf<String>()
        val ops = Stack<String>()
        val prec = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2, "%" to 2)

        for (t in tokens) {
            when {
                t.toIntOrNull() != null
                        || env.containsKey(t)
                        || arrayRegex.matches(t) ->
                    out += t

                t in prec -> {
                    while (ops.isNotEmpty() && ops.peek() in prec
                        && prec[ops.peek()]!! >= prec[t]!!) {
                        out += ops.pop()
                    }
                    ops.push(t)
                }

                t == "(" -> ops.push(t)

                t == ")" -> {
                    while (ops.peek() != "(") {
                        out += ops.pop()
                    }
                    ops.pop()
                }

                else -> error("Unknown token: $t")
            }
        }
        while (ops.isNotEmpty()) out += ops.pop()
        return out
    }

}