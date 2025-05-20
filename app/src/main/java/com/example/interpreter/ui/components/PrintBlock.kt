package com.example.interpreter.ui.components

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
import com.example.interpreter.model.Value
import com.example.interpreter.model.Variable

@Composable
fun PrintBlock() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp)
            .size(100.dp, 50.dp)
            .background(Color(59, 160, 255), RoundedCornerShape(8.dp)),
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
                    .padding(40.dp, 0.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color(70, 106, 140), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Var", color = Color.LightGray, fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.lato)))

            }
        }
    }
}