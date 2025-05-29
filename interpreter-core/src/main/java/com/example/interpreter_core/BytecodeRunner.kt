package com.example.interpreter_core


data class RunResult(val env: Map<String, Int>, val stack: List<Int>,val arrays: Map<String, List<Int>>)

object BytecodeRunner {
    fun run(program: List<Instruction>) : RunResult{
        val stack = java.util.Stack<Int>()
        val arrays = mutableMapOf<String, IntArray>()
        val env = mutableMapOf<String, Int>()
        var ip = 0

        while (ip < program.size) {
            when (val instr = program[ip]) {
                is Instruction.Var  -> { env[instr.name] = 0; ip++ }
                is Instruction.Push -> {
                    val tok = instr.token
                    val v = tok.toIntOrNull() ?: env[tok]
                    ?: error("Unknown token $tok")
                    stack.push(v); ip++
                }
                Instruction.Add    -> { val b = stack.pop(); val a = stack.pop(); stack.push(a + b); ip++ }
                Instruction.Sub    -> { val b = stack.pop(); val a = stack.pop(); stack.push(a - b); ip++ }
                Instruction.Mul    -> { val b = stack.pop(); val a = stack.pop(); stack.push(a * b); ip++ }
                Instruction.Div    -> { val b = stack.pop(); val a = stack.pop(); stack.push(a / b); ip++ }
                Instruction.Mod    -> { val b = stack.pop(); val a = stack.pop(); stack.push(a % b); ip++ }
                is Instruction.Pop -> { env[instr.target] = stack.pop(); ip++ }
                is Instruction.IfFalse -> {
                    val cond = stack.pop()
                    ip = if (cond != 0) ip + 1 else instr.targetIp
                }
                is Instruction.Goto  -> ip = instr.targetIp
                is Instruction.Cmp -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    val result = when (instr.op) {
                        ">" -> if (a > b) 1 else 0
                        "<" -> if (a < b) 1 else 0
                        ">=" -> if (a >= b) 1 else 0
                        "<=" -> if (a <= b) 1 else 0
                        "==" -> if (a == b) 1 else 0
                        "!=" -> if (a != b) 1 else 0
                        else -> error("Unknown cmp ${instr.op}")
                    }
                    stack.push(result)
                    ip++
                }
                is Instruction.Else    -> { ip++; continue }
                is Instruction.EndElse -> { ip++; continue }
                is Instruction.ArrayDecl -> {
                    arrays[instr.name] = IntArray(instr.size) { 0 }
                    ip++
                }
                is Instruction.ArrayLoad -> {
                    val idx = stack.pop()
                    val arr = arrays[instr.name] ?: error("No such array ${instr.name}")
                    if (idx !in arr.indices){
                        error("Index out of range: ${instr.name}[$idx], size=${arr.size}")
                    }
                    stack.push(arr[idx])
                    ip++
                }
                is Instruction.ArrayStore -> {
                    val value = stack.pop()
                    val idx   = stack.pop()
                    val arr = arrays[instr.name] ?: error("No such array ${instr.name}")
                    if (idx !in arr.indices){
                        error("Index out of range: ${instr.name}[$idx], size=${arr.size}")
                    }
                    arr[idx] = value
                    ip++
                }

                Instruction.End     -> break
            }
        }
        return RunResult(env.toMap(), stack.toList(), arrays.mapValues { it.value.toList() })
    }
}