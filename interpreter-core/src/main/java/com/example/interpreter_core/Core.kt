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
        "var a,b",
        "array f 3",
        "assign f[2] 3",
        "assign b f[2]",
    )
    val source3 = listOf(
        "array a 5",
        "assign a[0] 5",
        "assign a[1] 3",
        "assign a[2] 4",
        "assign a[3] 1",
        "assign a[4] 2",

        "var i,j,n,tmp",
        "assign n 5",
        "assign i 0",

        "while i < n",
        "assign j 0",
        "while j < n-i-1",

        "if a[j] > a[j+1]",
        "assign tmp a[j]",
        "assign a[j] a[j+1]",
        "assign a[j+1] tmp",
        "endif",

        "assign j j + 1",
        "endwhile",

        "assign i i + 1",
        "endwhile"
    )

    val program = BytecodeGenerator.parse(source)
    println("request \n" + source)
    for (a in program){
        println(a.toString())

    }
    val out = BytecodeRunner.run(program)
    println("Final env = ${out.env}")
    println("Final stack = ${out.stack}")
    println("Final arrays = ${out.arrays}")
}