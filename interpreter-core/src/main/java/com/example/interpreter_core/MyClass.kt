package com.example.interpreter_core

fun main() {
    println("== Interpreter (beta) ==")
    println("  • assign <var> <value>")
    println("  • plus   <a> <b>")
    println("  • mult   <a> <b>")
    println("  • env → current env")
    println("  • stack → current stack")
    println("  • exit → out.")

    val stack = mutableListOf<Int>()
    val env = mutableMapOf<String, Int>()

    while (true) {
        print("> ")
        val line = readLine()?.trim().orEmpty()
        if (line.isEmpty()) continue
        if (line.equals("exit", true)) break
        when (val parts = line.split("\\s+".toRegex())) {
            else -> {
                try {
                    val cmd = parts[0].lowercase()
                    val instr = when (cmd) {
                        "assign" -> {
                            require(parts.size >= 3)
                            val name = parts[1]
                            val expr = parts.subList(2, parts.size)
                            OperatorAssign(name, expr)
                        }
                        "plus" -> {
                            require(parts.size == 3)
                            OperatorPlus(parts[1], parts[2])
                        }
                        "stack" -> {
                            println("Current stack: $stack")
                            continue
                        }
                        "mult" -> {
                            require(parts.size == 3)
                            OperatorMultiplication(parts[1], parts[2])
                        }
                        "env" -> {
                            println("Current env: $env")
                            continue
                        }
                        else -> error("Wrong command '$cmd'")
                    }
                    instr.execute(stack, env)
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }
        }
    }
    println("aborting...")
}