package com.example.interpreter.model

import Utils.Exceptions

sealed class Block(open val id: String) {
    abstract fun command(): String

    data class VariableBlock(
        override val id: String,
        val variable: Variable
    ) : Block(id) {
        override fun command(): String {
            try {
                return "var ${variable.name}"
            }
            catch (e: Exception)
            {
                Exceptions.handleException("Var name was null")
                return ""
            }
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
            try{
                var rightCommand = if(right is Variable) (right as Variable).name else (right as Value).value
                return "assign ${(left as Variable).name} $rightCommand"
            }
            catch (e: Exception)
            {
                Exceptions.handleException("Null assignment")
                return ""
            }
        }
    }
    data class ConditionBlock(
        override val id: String,
        var leftExpr: IPlacable? = null,
        var rightExpr: IPlacable? = null,
        var operator: String
    ) : Block(id) {
        override fun command(): String {
            try {
                var rightCommand = if(rightExpr is Variable) (rightExpr as Variable).name else (rightExpr as Value).value
                var leftCommand = if(leftExpr is Variable) (leftExpr as Variable).name else (leftExpr as Value).value
                return "if $leftCommand $operator $rightCommand"
            }
            catch (e: Exception)
            {
                Exceptions.handleException("One of expression was null")
                return ""
            }

        }
    }

    data class WhileBlock(
        override val id: String,
        var leftExpr: IPlacable? = null,
        var rightExpr: IPlacable? = null,
        var operator: String
    ) : Block(id) {
        override fun command(): String {
            try {
                var rightCommand = if(rightExpr is Variable) (rightExpr as Variable).name else (rightExpr as Value).value
                var leftCommand = if(leftExpr is Variable) (leftExpr as Variable).name else (leftExpr as Value).value
                return "while $leftCommand $operator $rightCommand"
            }
            catch (e: Exception)
            {
                Exceptions.handleException("One of expression was null")
                return ""
            }

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
    data class CreatingArrayBlock(
        override val id: String,
        var name: String,
        var size: String
    ) : Block(id) {
        override fun command(): String {
            return "array $name $size"
        }
    }
    data class AssignArrBlock(
        override val id: String,
        var arr: Array? = null,
        var index: String = "0",
        var value: Value? = null
    ) : Block(id) {
        override fun command(): String {
            return "assign ${arr?.name}[$index] $value"
        }
    }
}
