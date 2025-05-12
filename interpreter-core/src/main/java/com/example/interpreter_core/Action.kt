package com.example.interpreter_core

interface Action {
    fun execute(stack: MutableList<Int>, env: MutableMap<String, Int>)
}