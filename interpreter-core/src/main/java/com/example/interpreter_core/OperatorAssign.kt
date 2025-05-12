package com.example.interpreter_core

class OperatorAssign (private val name: String, private val value: String) : Action{
    override fun execute(): String  = "$name $value ="
}