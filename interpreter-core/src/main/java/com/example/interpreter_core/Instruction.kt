package com.example.interpreter_core

sealed class Instruction {
    data class Var(val name: String) : Instruction()
    data class Push(val token: String) : Instruction()
    object Add : Instruction()
    object Sub : Instruction()
    object Mul : Instruction()
    object Div : Instruction()
    object Mod : Instruction()
    data class Pop(val target: String) : Instruction()
    data class IfFalse(val targetIp: Int) : Instruction()
    data class Goto(val targetIp: Int) : Instruction()
    object End : Instruction()
    data class Cmp(val op: String) : Instruction()

}