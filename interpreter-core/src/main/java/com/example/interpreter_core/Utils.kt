package com.example.interpreter_core
import java.util.Stack


fun infixToRpn(tokens: List<String>, env: MutableMap<String, Int>): List<String> {
    val output = mutableListOf<String>()
    val ops = Stack<String>()
    val prior = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)

    for (t in tokens) {
        when {
            t.toIntOrNull() != null || env.containsKey(t) -> output += t
            t in prior -> {
                while (ops.isNotEmpty() && ops.peek() != "(" && prior[ops.peek()]!! >= prior[t]!!) {
                    output += ops.pop()
                }
                ops.push(t)
            }
            t == "(" -> ops.push(t)
            t == ")" -> {
                while (ops.peek() != "(") {
                    output += ops.pop()
                }
                ops.pop()
            }
            else -> error("Unknown token $t")
        }
    }
    while (ops.isNotEmpty()) output += ops.pop()
    return output
}


fun evaluateRpn(rpn: List<String>, env: Map<String, Int>): Int {
    val stack = Stack<Int>()
    for (token in rpn) {
        val number = token.toIntOrNull() ?: env[token]
        if (number != null) {
            stack.push(number)
        } else {
            val b = stack.pop()
            val a = stack.pop()
            val result = when (token) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> a / b
                else -> error("Unknown operator '$token'")
            }
            stack.push(result)
        }
    }
    return stack.pop()
}

