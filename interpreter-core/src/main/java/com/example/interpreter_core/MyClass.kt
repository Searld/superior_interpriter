package com.example.interpreter_core

fun main() {
    println("== Interpreter (beta) ==")
    println("  • assign <var> <value>")
    println("  • plus   <a>    <b>")
    println("Empty string → out.")

    val actions = mutableListOf<Action>()
    while (true) {
        print("> ")
        val line = readLine()?.trim().orEmpty()
        if (line.isEmpty()) break

        val parts = line.split("\\s+".toRegex())
        when (parts[0].lowercase()) {
            "assign" -> {
                if (parts.size != 3) {
                    println("Error. Format: assign <val> <value>")
                    continue
                }
                actions += OperatorAssign(parts[1], parts[2])
            }
            "plus" -> {
                if (parts.size != 3) {
                    println("Error. Format: plus <a> <b>")
                    continue
                }
                actions += OperatorPlus(parts[1], parts[2])
            }
            else -> {
                println("Wrong command: ${parts[0]}")
            }
        }
    }

    val result = actions.joinToString(" ") { it.execute() }
    println(">>> Result: $result")
}