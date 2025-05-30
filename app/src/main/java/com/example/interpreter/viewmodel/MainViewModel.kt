package com.example.interpreter.viewmodel

import Utils.Exceptions
import android.R.bool
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interpreter.model.Array
import com.example.interpreter.model.Block
import com.example.interpreter.model.IPlacable
import com.example.interpreter.model.SelectedSlot
import com.example.interpreter.model.Value
import com.example.interpreter.model.Variable
import com.example.interpreter_core.BytecodeGenerator
import com.example.interpreter_core.BytecodeRunner
import com.example.interpreter_core.RunResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel : ViewModel() {
    private val _blocks = mutableStateListOf<Block>()
    val blocks: List<Block> get() = _blocks

    private val _variables = mutableStateListOf<Variable>()
    val variables: List<Variable> get() = _variables

    private val _arrays = mutableStateListOf<Array>()
    val arrays: List<Array> get() = _arrays

    var selectedItem = mutableStateOf<String?>(null)
        private set

    private val _selectedSlot = mutableStateOf<SelectedSlot?>(null)
    val selectedSlot by _selectedSlot

    fun selectSlot(blockId: String, slot: String) {
        _selectedSlot.value = SelectedSlot(blockId, slot)
    }

    private val _isIfClosed = mutableStateOf(false)
    val isIfClosed: Boolean get() = _isIfClosed.value

    private val _isIfExist = mutableStateOf(false)
    val isIfExist: Boolean get() = _isIfExist.value

    private val _isElseClosed = mutableStateOf(false)
    val isELseClosed: Boolean get() = _isElseClosed.value

    private val _isWhileClosed = mutableStateOf(false)
    val isWhileClosed: Boolean get() = _isWhileClosed.value

    private val _output = mutableStateOf<RunResult?>(null)
    val output: RunResult? get() = _output.value

    fun executeSource(lines: List<String>, onError: (String) -> Unit) = viewModelScope.launch {
        try {
            val program = BytecodeGenerator.parse(lines)
            _output.value = BytecodeRunner.run(program)
        } catch (e: Exception) {
            onError(e.message ?: "Unknown error")
        }
    }

    fun insertIntoSelectedSlot(value: IPlacable) {
        val slot = _selectedSlot.value ?: return
        val index = _blocks.indexOfFirst { it.id == slot.blockId }
        if (index == -1) return

        val oldBlock = _blocks[index]
        try {
             val newBlock = when (oldBlock){
                is Block.AssignmentBlock -> when(slot.slot){
                    "left" -> oldBlock.copy(left = value)
                    "right" -> oldBlock.copy(right = value)
                    else -> oldBlock
                }
                is Block.ConditionBlock -> when(slot.slot){
                    "left" -> oldBlock.copy(leftExpr = value)
                    "right" -> oldBlock.copy(rightExpr = value)
                    else -> oldBlock
                }
                is Block.AssignArrBlock -> when(slot.slot){
                    "left" -> oldBlock.copy(arrName = (value as Array).name)
                    "right" -> oldBlock.copy(value = value as Value)
                    else -> oldBlock
                }
                is Block.PrintBlock -> oldBlock.copy(variable = value as Variable)
                else -> oldBlock
            }
            _blocks[index] = newBlock
            clearSelectedSlot()
        }
        catch (e: Exception)
        {
            Exceptions.handleException("Invalid type for insert")
        }
    }

    fun removeBlock(blockId: String) {
        var block = _blocks.find{it.id == blockId}
        if(block is Block.CreatingArrayBlock)
            _arrays.removeAll { it.name == block.name }
        if(block is Block.VariableBlock)
            _variables.removeAll { it.name == block.variable.name }
        _blocks.removeAll { it.id == blockId }
    }

    fun clearSelectedSlot() {
        _selectedSlot.value = null
    }

    fun addVariable(name: String, value: String? = null) {
        val variable = Variable(name,value)
        _variables.add(variable)
        _blocks.add(0,Block.VariableBlock(UUID.randomUUID().toString(), variable))
    }

    fun addAssignmentBlock(left: IPlacable? = null, right: IPlacable? = null) {
        _blocks.add(Block.AssignmentBlock(UUID.randomUUID().toString(), left = left, right=right))
    }

    fun addAssignArrayBlock(arrName: String? = null, index: String = "0", value: Value? = null) {
        _blocks.add(Block.AssignArrBlock(UUID.randomUUID().toString(), arrName= arrName,
            index = index, value=value))
    }

    fun addConditionBlock(operator: String, leftExpr: IPlacable? = null, rightExpr: IPlacable? = null) {
        _blocks.add(Block.ConditionBlock(UUID.randomUUID().toString(),
            operator = operator, leftExpr = leftExpr, rightExpr = rightExpr))
    }

    fun addPrintBlock(variable: Variable? = null) {
        _blocks.add(Block.PrintBlock(UUID.randomUUID().toString(), variable = variable))
    }

    fun addWhileBlock(operator:String, leftExpr: IPlacable?=null, rightExpr: IPlacable? = null) {
        _blocks.add(Block.WhileBlock(UUID.randomUUID().toString(),
            operator = operator, leftExpr = leftExpr, rightExpr = rightExpr))
    }

    fun addCreatingArrayBlock(arrName: String, arrSize: String) {
        val arr = Array(arrName, arrSize)
        _arrays.add(arr)
        _blocks.add(Block.CreatingArrayBlock(UUID.randomUUID().toString(), arrName, arrSize))
    }

    fun addEndifBlock() {
        blocks.reversed().forEach { block ->
            if(block is Block.EndifBlock)
                return

            if(block is Block.ElseBlock || block is Block.ConditionBlock) {
                _blocks.add(Block.EndifBlock(UUID.randomUUID().toString()))
                return
            }

        }
    }

    fun addEndwhileBlock() {
        blocks.reversed().forEach { block ->
            if(block is Block.EndWhile)
                return

            if( block is Block.WhileBlock)
                _blocks.add(Block.EndWhile(UUID.randomUUID().toString()))
        }
    }

    fun addElseBlock() {
        blocks.reversed().forEach { block ->
            if(block is Block.ElseBlock || block is Block.EndifBlock)
                return

            if( block is Block.ConditionBlock)
                _blocks.add(Block.ElseBlock(UUID.randomUUID().toString()))

        }
    }

    fun onItemSelected(item: String?) {
        selectedItem.value = item
    }

    fun loadBlocks(commands: List<String>)
    {
        val regex = Regex("""^[a-zA-Z_][a-zA-Z0-9_]*$""")
        commands.forEach { command ->
            var splited = command.split(" ")
            when(splited[0]) {
                "var" -> addVariable(splited[1])
                "endif" -> addEndifBlock()
                "assign" -> if(command.contains("["))
                    addAssignArrayBlock(splited[1].split("[")[0],
                        splited[1].split("[")[1].split("]")[1], Value(splited[2]))
                    else addAssignmentBlock(Variable(splited[1],""),
                        if(regex.matches(splited[2])) Variable(splited[2],"") else
                    Value(splited[2]))
                "if" -> addConditionBlock(splited[2],
                    if(regex.matches(splited[1])) Variable(splited[1],"")
                    else Value(splited[1]),
                    if(regex.matches(splited[3])) Variable(splited[3],"")
                    else Value(splited[3])
                )
                "while" -> addWhileBlock(
                    splited[2],
                    if(regex.matches(splited[1])) Variable(splited[1],"")
                    else Value(splited[1]),
                    if(regex.matches(splited[3])) Variable(splited[3],"")
                    else Value(splited[3])
                )
                "print" -> addPrintBlock(Variable(splited[1],""))
                "array" -> addCreatingArrayBlock(splited[1], splited[2])
            }
        }
    }
}