package com.example.interpreter_core

fun main() {
    println("== Interpreter (console) ==")
    println("  • var <a,b,...>       — declare variables (=0)")
    println("  • assign <var> <expr> — assignment variables")
    println("  • plus   <a> <b>      — sum")
    println("  • mult   <a> <b>      — multiplication")
    println("  • stack              — show stack")
    println("  • env                — show variables")
    println("  • exit               — exit the app")

    val stack = mutableListOf<Int>()
    val env = mutableMapOf<String, Int>()

    while (true) {
        print("> ")
        val line = readLine()?.trim().orEmpty()
        if (line.isEmpty()) continue
        if (line.equals("exit", true)) break
        val parts = line.split("\\s+".toRegex())
        try {
            when (parts[0].lowercase()) {
                "var" -> {
                    val names = parts[1].split(",").map { it.trim() }
                    OperatorVar(names).execute(stack,env)
                }
                "assign" -> {
                    require(parts.size >= 3)
                    val name = parts[1]
                    val expr = parts.subList(2, parts.size)
                    OperatorAssign(name, expr).execute(stack, env)
                }
                "plus" -> OperatorPlus(parts[1], parts[2]).execute(stack, env)
                "mult" -> OperatorMultiplication(parts[1], parts[2]).execute(stack, env)
                "stack" -> println("Current stack: $stack")
                "env" -> println("Current env: $env")
                else -> error("Wrong command '${parts[0]}'")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
    println("Exiting...")
}
