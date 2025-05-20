package com.example.interpreter.model

sealed class Block(open val id: String) {
    data class VariableBlock(override val id: String, val variable: Variable) : Block(id)
    data class AssignmentBlock(
        override val id: String,
        val left: IPlacable? = null,
        val right: IPlacable? = null
    ) : Block(id)
}