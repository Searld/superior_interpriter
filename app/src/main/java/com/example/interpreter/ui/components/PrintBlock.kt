package com.example.interpreter.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.interpreter.R
import com.example.interpreter.model.Block
import com.example.interpreter.model.Value
import com.example.interpreter.model.Variable
import com.example.interpreter.viewmodel.MainViewModel

@Composable
fun PrintBlock(viewModel: MainViewModel,
               block: Block.PrintBlock) {
    val selectedSlot = viewModel.selectedSlot
    val isSelected = selectedSlot?.blockId == block.id
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp)
            .size(100.dp, 50.dp)
            .border(
                BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = Color(59, 160, 255).copy(alpha = 0.07f),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            Text("Print", color = Color.White, fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.lato)),
                modifier = Modifier.padding(start = 15.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
                    .padding(10.dp, 0.dp)
                    .border(
                        width = 1.dp,
                        color = if(isSelected) Color.Red.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color(70, 106, 140).copy(alpha = 0.09f), shape = RoundedCornerShape(8.dp))
                    .clickable {
                        if(!isSelected)
                            viewModel.selectSlot(block.id,"")
                        else
                            viewModel.clearSelectedSlot()
                    },
                contentAlignment = Alignment.Center
            ) {
                val txt = if(block.variable!=null) block.variable?.name else "Var or arr"
                Text(text = txt.toString(), color = Color.LightGray, fontSize = 11.sp)
            }
        }
    }
}