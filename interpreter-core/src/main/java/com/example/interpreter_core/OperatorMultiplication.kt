package com.example.interpreter_core

class OperatorMultiplication (
    private val aToken: String,
    private val bToken: String
) : Action {
    override fun execute(stack: MutableList<Int>, env: MutableMap<String, Int>) {
        val a = aToken.toIntOrNull() ?: env[aToken] ?: error("Unknown variable $aToken")
        val b = bToken.toIntOrNull() ?: env[bToken] ?: error("Unknown variable $bToken")
        val sum = a * b;
        stack.add(sum);
        println("[multiplication] stack: $sum ($a+$b)")
    }
}