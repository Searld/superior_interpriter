package com.example.interpreter_core

import java.util.Stack

object Utils {
    fun tokenize(expr: String): List<String> =
        expr.replace("(", " ( ")
            .replace(")", " ) ")
            .replace("+", " + ")
            .replace("-", " - ")
            .replace("*", " * ")
            .replace("/", " / ")
            .replace("%", " % ")
            .trim().split("\\s+".toRegex())

    fun toRPN(tokens: List<String>, env: Map<String, Int>): List<String> {
        val out = mutableListOf<String>()
        val ops = Stack<String>()
        val prec = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2, "%" to 2)
        for (t in tokens) {
            when {
                t.toIntOrNull() != null || env.containsKey(t) -> out += t
                t in prec -> {
                    while (ops.isNotEmpty() && ops.peek() in prec && prec[ops.peek()]!! >= prec[t]!!) {
                        out += ops.pop()
                    }
                    ops.push(t)
                }
                t == "(" -> ops.push(t)
                t == ")" -> {
                    while (ops.peek() != "(") out += ops.pop()
                    ops.pop()
                }
                else -> error("Unknown token $t")
            }
        }
        while (ops.isNotEmpty()) out += ops.pop()
        return out
    }

}