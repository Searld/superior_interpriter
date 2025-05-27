package com.example.interpreter_core

object BytecodeGenerator {
    fun parse(lines: List<String>): List<Instruction> {
        val program = mutableListOf<Instruction>()

        val ifStack = mutableListOf<Int>()
        val ifFalseStack = mutableListOf<Int>()
        val gotoStack    = mutableListOf<Int>()

        for (raw in lines) {
            val line = raw.trim()
            if (line.isEmpty()) continue
            val parts = line.split(" ", limit = 2)
            val arg   = parts.getOrNull(1) ?: ""
            when (parts[0].lowercase()) {
                "var" -> {
                    parts.getOrNull(1)?.split(",")?.map { it.trim() }
                        ?.forEach { program += Instruction.Var(it) }
                }
                "assign" -> {
                    val assignParts = parts.getOrNull(1)
                        ?.split(" ", limit = 2)
                        ?: error("Bad assign syntax")
                    val name = assignParts[0]
                    val expr = assignParts[1]
                    val tokens = Utils.tokenize(expr)
                    val env = program.filterIsInstance<Instruction.Var>().associate { it.name to 0 }
                    val rpn = Utils.toRPN(tokens, env)
                    for (tok in rpn) {
                        when (tok) {
                            "+" -> program += Instruction.Add
                            "-" -> program += Instruction.Sub
                            "*" -> program += Instruction.Mul
                            "/" -> program += Instruction.Div
                            "%" -> program += Instruction.Mod
                            else -> program += Instruction.Push(tok)
                        }
                    }
                    program += Instruction.Pop(name)
                }
                "if" -> {
                    val hdr = arg.split(" ", limit = 3)
                    val cmp = hdr[1]
                    val cmpParts = parts[1].split(" ", limit = 3)

                    val env = program.filterIsInstance<Instruction.Var>().associate { it.name to 0 }
                    val lhsRpn = Utils.toRPN(Utils.tokenize(cmpParts[0]), env)
                    val rhsRpn = Utils.toRPN(Utils.tokenize(cmpParts[2]), env)
                    lhsRpn.forEach { tok -> emitToken(tok, program) }
                    rhsRpn.forEach { tok -> emitToken(tok, program) }

                    program += Instruction.Cmp(cmp)

                    program += Instruction.IfFalse(-1)
                    ifFalseStack += program.lastIndex
                }
                "endif" -> {
                    if (ifFalseStack.isNotEmpty()) {
                        val ifFalsePos = ifFalseStack.removeLast()
                        program[ifFalsePos] = Instruction.IfFalse(program.size)
                    }
                }
                "else" -> {
                    program += Instruction.Goto(-1)
                    gotoStack += program.lastIndex

                    val ifFalsePos = ifFalseStack.removeLast()
                    program[ifFalsePos] = Instruction.IfFalse(program.size)
                }
                "endelse"->{
                    val gotoPos=gotoStack.removeLast()
                    program[gotoPos]=Instruction.Goto(program.size)
                }
                "exit" -> program += Instruction.End
                else -> error("Unknown cmd ${parts[0]}")
            }
        }
        program += Instruction.End
        return program
    }
    private fun emitToken(tok: String, prog: MutableList<Instruction>) {
        tok.toIntOrNull()?.let {
            prog += Instruction.Push(it.toString())
            return
        }
        when (tok) {
            "+","-","*","/","%" -> prog += when(tok){
                "+"->Instruction.Add; "-"->Instruction.Sub
                "*"->Instruction.Mul; "/"->Instruction.Div; "%"->Instruction.Mod
                else->error("")
            }
            else -> prog += Instruction.Push(tok)
        }
    }

}
