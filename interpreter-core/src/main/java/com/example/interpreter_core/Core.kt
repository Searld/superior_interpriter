package com.example.interpreter_core

fun main() {
    val source = listOf(
        "var a,b,c",
        "assign a 3+4",
        "assign b a*2",
        "if b-100 > 10",
        "  assign c b+3",
        "  assign c c+3",
        "endif",
        "assign f 99999",
        "assign b 99999",
        "exit"
    )
    val source1 = listOf(
        "var a,b,c",
        "assign a 3+4",
        "assign b a*2",
        "assign c b * a",
    )
    val program = BytecodeGenerator.parse(source)
    println("request \n" + source)
    for (a in program){
        println(a.toString())

    }
    BytecodeRunner.run(program)
}