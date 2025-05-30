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
                    "left" -> oldBlock.copy(arr = value as Array)
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

    fun addVariable(name: String) {
        val variable = Variable(name,"0")
        _variables.add(variable)
        _blocks.add(0,Block.VariableBlock(UUID.randomUUID().toString(), variable))
    }

    fun addAssignmentBlock() {
        _blocks.add(Block.AssignmentBlock(UUID.randomUUID().toString()))
    }

    fun addAssignArrayBlock() {
        _blocks.add(Block.AssignArrBlock(UUID.randomUUID().toString()))
    }

    fun addConditionBlock(operator: String) {
        _blocks.add(Block.ConditionBlock(UUID.randomUUID().toString(), operator = operator))
    }

    fun addPrintBlock() {
        _blocks.add(Block.PrintBlock(UUID.randomUUID().toString()))
    }

    fun addWhileBlock(operator:String) {
        _blocks.add(Block.WhileBlock(UUID.randomUUID().toString(), operator = operator))
    }

    fun addCreatingArrayBlock(arrName: String, arrSize: String) {
        val arr = Array(arrName, arrSize)
        _arrays.add(arr)
        _blocks.add(Block.CreatingArrayBlock(UUID.randomUUID().toString(), arrName, arrSize))
    }

    fun addEndifBlock() {
        if(!isIfClosed) {
            _blocks.add(Block.EndifBlock(UUID.randomUUID().toString()))
            _isIfClosed.value = true
        }
    }

    fun onItemSelected(item: String?) {
        selectedItem.value = item
    }
}