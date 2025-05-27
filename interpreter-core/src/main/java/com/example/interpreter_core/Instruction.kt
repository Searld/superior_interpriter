package com.example.interpreter_core

sealed class Instruction {
    data class Var(val name: String) : Instruction()
    data class Push(val token: String) : Instruction()
    data object Add : Instruction()
    data object Sub : Instruction()
    data object Mul : Instruction()
    data object Div : Instruction()
    data object Mod : Instruction()
    data class Pop(val target: String) : Instruction()
    data class IfFalse(val targetIp: Int) : Instruction()
    data class Goto(val targetIp: Int) : Instruction()
    data object End : Instruction()
    data class Cmp(val op: String) : Instruction()
    object Else : Instruction()
    object EndElse : Instruction()

}