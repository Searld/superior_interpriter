package com.example.interpreter.model

sealed class Block(open val id: String) {
    abstract fun command(): String

    data class VariableBlock(
        override val id: String,
        val variable: Variable
    ) : Block(id) {
        override fun command(): String {
            return "var ${variable.name}"
        }
    }

    data class EndifBlock(
        override val id: String
    ) : Block(id) {
        override fun command(): String {
            return "endif"
        }
    }

    data class AssignmentBlock(
        override val id: String,
        var left: IPlacable? = null,
        var right: IPlacable? = null
    ) : Block(id) {
        override fun command(): String {
            var rightCommand = if(right is Variable) (right as Variable).name else (right as Value).value
            return "assign ${(left as Variable).name} $rightCommand"
        }
    }
    data class ConditionBlock(
        override val id: String,
        var leftExpr: IPlacable? = null,
        var rightExpr: IPlacable? = null
    ) : Block(id) {
        override fun command(): String {
            var rightCommand = if(rightExpr is Variable) (rightExpr as Variable).name else (rightExpr as Value).value
            var leftCommand = if(leftExpr is Variable) (leftExpr as Variable).name else (leftExpr as Value).value
            return "if $leftCommand > $rightCommand"
        }
    }
    data class PrintBlock(
        override val id: String,
        var variable: Variable? = null
    ) : Block(id) {
        override fun command(): String {
            return ""
        }
    }
}
