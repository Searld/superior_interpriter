package com.example.interpreter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interpreter.model.Block
import com.example.interpreter.model.IPlacable
import com.example.interpreter.model.SelectedSlot
import com.example.interpreter.model.Value
import com.example.interpreter.model.Variable
import com.example.interpreter_core.BytecodeGenerator
import com.example.interpreter_core.BytecodeRunner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel : ViewModel() {
    private val _blocks = mutableStateListOf<Block>()
    val blocks: List<Block> get() = _blocks

    private val _variables = mutableStateListOf<Variable>()
    val variables: List<Variable> get() = _variables

    var selectedItem = mutableStateOf<String?>(null)
        private set

    private val _selectedSlot = mutableStateOf<SelectedSlot?>(null)
    val selectedSlot by _selectedSlot

    fun selectSlot(blockId: String, slot: String) {
        _selectedSlot.value = SelectedSlot(blockId, slot)
    }

    private val _output = mutableStateOf<String>("")
    val output: String get() = _output.value

    fun executeSource(lines: List<String>) = viewModelScope.launch {
        try {
            val program = BytecodeGenerator.parse(lines)
            val result = BytecodeRunner.run(program)

            _output.value = "==== Bytecode Instructions ====\n"
            program.forEachIndexed { idx, instr -> _output.value += "$idx: $instr\n" }
            _output.value += "==== Bytecode Instructions End====\n\n"
            _output.value += "==== Final Environment ====\n"
            result.env.forEach { (name, value) -> _output.value += "$name = $value\n" }
            _output.value += "==== Final Environment End====\n\n"
            _output.value += "==== Final Stack ====\n"
            result.stack.forEachIndexed { idx, v -> _output.value += "[$idx] = $v" }
            _output.value += "==== Final Stack End====\n"


        } catch (e: Exception) {
            _output.value = "Ошибка: ${e.message}"
        }
    }

    fun insertIntoSelectedSlot(value: IPlacable) {
        val slot = _selectedSlot.value ?: return
        val index = _blocks.indexOfFirst { it.id == slot.blockId }
        if (index == -1) return

        val oldBlock = _blocks[index]
        val newBlock = when (oldBlock){
            is Block.AssignmentBlock -> when(slot.slot){
                "left" -> oldBlock.copy(left = value)
                "right" -> oldBlock.copy(right = value)
                else -> oldBlock
            }
            else -> oldBlock
        }

        _blocks[index] = newBlock
        clearSelectedSlot()
    }

    fun clearSelectedSlot() {
        _selectedSlot.value = null
    }

    fun addVariable(name: String, command: String) {
        val variable = Variable(name,"0")
        _variables.add(variable)
        _blocks.add(0,Block.VariableBlock(UUID.randomUUID().toString(), variable, command))
    }

    fun addAssignmentBlock( command: String) {
        _blocks.add(Block.AssignmentBlock(UUID.randomUUID().toString(), command))
    }

    fun onItemSelected(item: String?) {
        selectedItem.value = item
    }
}