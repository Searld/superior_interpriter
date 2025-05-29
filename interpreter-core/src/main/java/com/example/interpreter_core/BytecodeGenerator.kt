package com.example.interpreter_core

object BytecodeGenerator {
    fun parse(lines: List<String>): List<Instruction> {
        val program = mutableListOf<Instruction>()

        val ifStack = mutableListOf<Int>()
        val ifFalseStack = mutableListOf<Int>()
        val whileStartStack = mutableListOf<Int>()
        val whileExitStack = mutableListOf<Int>()
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
                    val (target, expr) = arg.split(" ", limit = 2)
                    val env = program.filterIsInstance<Instruction.Var>().associate { it.name to 0 }

                    val arrayMatch = Regex("""^(\w+)\[(.+)]$""").find(target)
                    if (arrayMatch != null) {
                        val (name, idxExpr) = arrayMatch.destructured

                        val idxTokens = Utils.tokenize(idxExpr)
                        val idxRpn = Utils.toRPN(idxTokens, env)
                        idxRpn.forEach { emitToken(it, program) }

                        val valueTokens = Utils.tokenize(expr)
                        val valueRpn = Utils.toRPN(valueTokens, env)
                        valueRpn.forEach { emitToken(it, program) }


                        program += Instruction.ArrayStore(name)
                    } else {
                        val rpn = Utils.toRPN(Utils.tokenize(expr), env)
                        rpn.forEach { emitToken(it, program) }
                        program += Instruction.Pop(target)
                    }
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
                "while" -> {
                    whileStartStack += program.size

                    val hdr = arg.split(" ", limit = 3)
                    val lhs = Utils.tokenize(hdr[0])
                    val cmp = hdr[1]
                    val rhs = Utils.tokenize(hdr[2])

                    val env = program.filterIsInstance<Instruction.Var>().associate { it.name to 0 }
                    Utils.toRPN(lhs, env).forEach { emitToken(it, program) }
                    Utils.toRPN(rhs, env).forEach { emitToken(it, program) }

                    program += Instruction.Cmp(cmp)
                    program += Instruction.IfFalse(-1)
                    whileExitStack += program.lastIndex
                }
                "endwhile" -> {
                    val loopStart = whileStartStack.removeLast()
                    program += Instruction.Goto(loopStart)

                    val exitPos = whileExitStack.removeLast()
                    program[exitPos] = Instruction.IfFalse(program.size)
                }
                "array" -> {
                    val (name, sizeStr) = arg.split(" ")
                    val size = sizeStr.toIntOrNull() ?: error("Wrong array size")
                    program += Instruction.ArrayDecl(name, size)
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
        Regex("""^(\w+)\[(.+)]$""").find(tok)?.let { m ->
            val (name, idxExpr) = m.destructured
            val env = prog.filterIsInstance<Instruction.Var>().associate { it.name to 0 }

            val idxRpn = Utils.toRPN(Utils.tokenize(idxExpr), env)
            idxRpn.forEach { emitToken(it, prog) }
            prog += Instruction.ArrayLoad(name)
            return
        }
        when (tok) {
            "+" -> prog += Instruction.Add
            "-" -> prog += Instruction.Sub
            "*" -> prog += Instruction.Mul
            "/" -> prog += Instruction.Div
            "%" -> prog += Instruction.Mod
            else -> prog += Instruction.Push(tok)
        }
    }



}
