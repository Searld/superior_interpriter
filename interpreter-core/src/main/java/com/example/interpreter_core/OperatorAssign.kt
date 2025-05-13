package com.example.interpreter_core
import java.util.Stack

class OperatorAssign(
    private val name: String, private val exprTokens: List<String>
) : Action {
    override fun execute(stack: MutableList<Int>, env: MutableMap<String, Int>) {
        val rpn = infixToRpn(exprTokens, env)
        val result = evaluateRpn(rpn, env)
        env[name] = result
        println("[assign] $name = $result")
    }
}
