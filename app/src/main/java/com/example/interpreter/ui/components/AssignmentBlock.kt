package com.example.interpreter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.interpreter.model.Block
import com.example.interpreter.model.IPlacable
import com.example.interpreter.model.Value
import com.example.interpreter.model.Variable
import com.example.interpreter.viewmodel.MainViewModel

@Composable
fun AssignmentBlock(
    block: Block.AssignmentBlock,
    viewModel: MainViewModel
) {
    val selectedSlot = viewModel.selectedSlot
    val isLeftSelected = selectedSlot?.blockId == block.id && selectedSlot.slot == "left"
    val isRightSelected = selectedSlot?.blockId == block.id && selectedSlot.slot == "right"

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp)
            .size(100.dp, 50.dp)
            .background(Color(59, 160, 255), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
                    .padding(10.dp, 0.dp)
                    .border(
                        width = 1.dp,
                        color = if (isLeftSelected) Color.Red else Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color(70, 106, 140), shape = RoundedCornerShape(8.dp))
                    .clickable {
                        if (isLeftSelected) viewModel.clearSelectedSlot()
                        else viewModel.selectSlot(block.id, "left")
                    },
                contentAlignment = Alignment.Center
            ) {
                val text = when (val left = block.left) {
                    is Variable -> left.name
                    is Value -> left.value
                    else -> "Var"
                }
                Text(text, color = Color.LightGray, fontSize = 11.sp)
            }

            Text("=", color = Color.White, fontSize = 25.sp)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
                    .padding(10.dp, 0.dp)
                    .border(
                        width = 1.dp,
                        color = if (isRightSelected) Color.Red else Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color(70, 106, 140), shape = RoundedCornerShape(8.dp))
                    .clickable {
                        if (isRightSelected) viewModel.clearSelectedSlot()
                        else viewModel.selectSlot(block.id, "right")
                    },
                contentAlignment = Alignment.Center
            ) {
                val text = when (val right = block.right) {
                    is Variable -> right.name
                    is Value -> right.value
                    else -> "Value or var"
                }
                Text(text, color = Color.LightGray, fontSize = 11.sp)
            }
        }
    }
}