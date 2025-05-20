package com.example.interpreter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun InitializeVarBlock(varName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp)
            .size(100.dp, 50.dp)
            .background(Color(59,160,255), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Text("var:",
                color = Color.White,
                fontSize = 25.sp,
                modifier = Modifier.padding(20.dp,0.dp,30.dp,0.dp),
                fontFamily = FontFamily(Font(R.font.lato)))
            Text("$varName",
                color = Color.White,
                fontSize = 23.sp,
                fontFamily = FontFamily(Font(R.font.lato)))
        }

    }
}