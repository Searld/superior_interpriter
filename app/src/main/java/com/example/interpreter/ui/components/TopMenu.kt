package com.example.interpreter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.interpreter.R

@Composable
fun TopMenu(
    selectedItem: MutableState<String?>,
    onItemSelected: (String) -> Unit
) {
    val items = listOf("Variables", "Conditions", "Loops", "Functions")
    val lazyListState = rememberLazyListState()

    LazyRow(
        state = lazyListState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState),
        modifier = Modifier
            .background(Color(5, 5, 5))
            .fillMaxWidth()
            .padding(top = 45.dp)
            .height(80.dp)
            .padding(8.dp)
    ) {
        items(items.size) { index ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(120.dp, 60.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .clickable {
                        onItemSelected(items[index])
                    }
                    .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = items[index],
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = FontFamily(Font(R.font.lato, FontWeight.Bold))
                )
            }
        }
    }
}
