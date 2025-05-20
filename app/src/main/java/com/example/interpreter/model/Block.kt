package com.example.interpreter.model

sealed class Block(open val id: String, open val command: String) {
    data class VariableBlock(override val id: String,
                             val variable: Variable,
                             override val command: String) : Block(id, command)
    data class AssignmentBlock(
        override val id: String,
        override val command: String,
        var left: IPlacable? = null,
        var right: IPlacable? = null
    ) : Block(id, command)
}