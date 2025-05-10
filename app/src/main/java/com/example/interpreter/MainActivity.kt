package com.example.interpreter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.interpreter.ui.theme.InterpreterTheme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.window.Popup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopMenu()

            WorkArea()

            BottomMenu()
        }
    }

    @Composable
    fun TopMenu() {
        val items = listOf("Operator +", "Operator -", "/", "*", "=")
        val lazyListState = rememberLazyListState()

        LazyRow(
            state = lazyListState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState),
            modifier = Modifier
                .background(Color(0xFF090f1f))
                .fillMaxWidth()
                .padding(top=45.dp)
                .height(80.dp)
                .padding(8.dp)

        ) {
            items(items.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(120.dp, 60.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center,

                ) {
                    Text(
                        text = items[index],
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    @Composable
    fun WorkArea() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .background(Color(0xFF090f1f))
                .pointerInput(Unit) {

                },
            contentAlignment = Alignment.Center
        )
        {

        }
    }

    @Composable
    fun BottomMenu() {
        val items = listOf("Main", "Run", "Settings")
        val icons = listOf(Icons.Default.Home, Icons.Default.Star, Icons.Default.Settings)
        var selectedItem by remember { mutableStateOf(0) }

        NavigationBar(containerColor = Color(0xFF202636)) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(icons[index], contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.White,
                        indicatorColor = Color(0xFF5e667a)
                    )
                )
            }
        }
    }


}

