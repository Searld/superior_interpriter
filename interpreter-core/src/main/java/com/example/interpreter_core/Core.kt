package com.example.interpreter_core

fun main() {
    val source1 = listOf(
        "var a,b,c",
        "assign a 3+4",
        "assign b a*2",
        "if b > 10",
        "  assign c b+3",
        "  assign c c+3",
        "endif",
        "assign f 2*(2+2)",
        "assign b 99999",
        "exit"
    )
    val source = listOf(
        "var a,b,c",
        "assign a 3+4",
        "assign b a*2",
        "while a > 2",
        "   assign a a - 2",
        "endwhile",
        "assign b 23",
    )
    val program = BytecodeGenerator.parse(source)
    println("request \n" + source)
    for (a in program){
        println(a.toString())

    }
    val out = BytecodeRunner.run(program)
    println("Final env = ${out.env}")
    println("Final stack = ${out.stack}")
}