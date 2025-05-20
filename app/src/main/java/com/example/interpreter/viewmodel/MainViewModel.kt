package com.example.interpreter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.interpreter.model.Block
import com.example.interpreter.model.IPlacable
import com.example.interpreter.model.SelectedSlot
import com.example.interpreter.model.Value
import com.example.interpreter.model.Variable
import java.util.UUID

class MainViewModel : ViewModel() {
    private val _blocks = mutableStateListOf<Block>()
    val blocks: List<Block> get() = _blocks

    private val _textForPrint = mutableStateOf<String>("")
    val textForPrint: String get() = _textForPrint.value

    private val _variables = mutableStateListOf<Variable>()
    val variables: List<Variable> get() = _variables

    var selectedItem = mutableStateOf<String?>(null)
        private set

    private val _selectedSlot = mutableStateOf<SelectedSlot?>(null)
    val selectedSlot by _selectedSlot

    fun selectSlot(blockId: String, slot: String) {
        _selectedSlot.value = SelectedSlot(blockId, slot)
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

    fun addVariable(name: String) {
        val variable = Variable(name,"0")
        _variables.add(variable)
        _blocks.add(Block.VariableBlock(UUID.randomUUID().toString(), variable))
    }

    fun addAssignmentBlock() {
        _blocks.add(Block.AssignmentBlock(UUID.randomUUID().toString()))
    }

    fun onItemSelected(item: String?) {
        selectedItem.value = item
    }
}