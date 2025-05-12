package com.example.interpreter_core

class OperatorPlus (private val first: String, private val second: String) : Action{
    override fun execute(): String = "$first $second +"
}