package com.example.interpreter_core
class OperatorVar(
    private val names: List<String>
) : Action {
    override fun execute(stack: MutableList<Int>, env: MutableMap<String, Int>) {
        names.forEach { name ->
            env[name] = 0
            println("[var] $name = 0")
        }
    }
}