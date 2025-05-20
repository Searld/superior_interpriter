package com.example.interpreter_core

object BytecodeGenerator {
    fun parse(lines: List<String>): List<Instruction> {
        val program = mutableListOf<Instruction>()
        val ifStack = mutableListOf<Int>()

        for (raw in lines) {
            val line = raw.trim()
            if (line.isEmpty()) continue
            val parts = line.split(" ", limit = 2)
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
                    val cmpParts = parts[1].split(" ", limit = 3)

                    val env = program.filterIsInstance<Instruction.Var>().associate { it.name to 0 }
                    val lhsRpn = Utils.toRPN(Utils.tokenize(cmpParts[0]), env)
                    val rhsRpn = Utils.toRPN(Utils.tokenize(cmpParts[2]), env)
                    lhsRpn.forEach { program += if (it in arrayOf("+","-","*","/","%"))
                        when (it) {
                            "+"->Instruction.Add;
                            "-"->Instruction.Sub;
                            "*"->Instruction.Mul;
                            "/"->Instruction.Div;
                            "%"->Instruction.Mod
                            else->Instruction.Push(it)
                        }
                        else Instruction.Push(it)
                    }
                    rhsRpn.forEach { program += if (it in arrayOf("+","-","*","/","%"))
                        when (it) {
                            "+"->Instruction.Add;
                            "-"->Instruction.Sub;
                            "*"->Instruction.Mul;
                            "/"->Instruction.Div;
                            "%"->Instruction.Mod
                            else->Instruction.Push(it)
                        }
                        else Instruction.Push(it)
                    }

                    program += Instruction.Cmp(cmpParts[1])
                    program += Instruction.IfFalse(-1)
                    ifStack += program.lastIndex
                }
                "endif" -> {
                    val ifPos = ifStack.removeLast()
                    val target = program.size
                    program[ifPos] = Instruction.IfFalse(target)
                }
                "exit" -> program += Instruction.End
                else -> error("Unknown cmd ${parts[0]}")
            }
        }
        program += Instruction.End
        return program
    }
}
