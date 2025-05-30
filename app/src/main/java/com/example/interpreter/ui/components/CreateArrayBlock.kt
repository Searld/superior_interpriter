package com.example.interpreter.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.interpreter.R
import com.example.interpreter.model.Block

@Composable
fun CreateArrayBlock(block: Block.CreatingArrayBlock) {
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
        var size by remember { mutableStateOf("") }
        block.size = size
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Text("array",
                color = Color.White,
                fontSize = 23.sp,
                fontFamily = FontFamily(Font(R.font.lato)),
                modifier = Modifier.padding(bottom = 3.dp))
            Text("${block.name}",
                color = Color.White,
                fontSize = 23.sp,
                fontFamily = FontFamily(Font(R.font.lato)),
                modifier = Modifier.padding(start=15.dp, end = 5.dp, bottom = 3.dp))
            BasicTextField(
                value = size,
                onValueChange = { size = it },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .width(30.dp)
                    .height(25.dp)
                    .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                    .background(Color(70, 106, 140).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(0.dp),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        innerTextField()
                    }
                }
            )
        }

    }
}