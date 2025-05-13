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
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import java.util.UUID
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }

    data class Block(val type: String, val id: String = UUID.randomUUID().toString(), val varName: String)
    @Composable
    fun MainScreen() {
        val blocks = remember { mutableStateListOf<Block>()}
        val addBlockHandler: (String,String) -> Unit = { blockType: String, varName: String ->
            val newBlock = Block(type = blockType, varName = varName)
            blocks.add(newBlock)
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopMenu(onAddBlock = addBlockHandler)

            WorkArea(blocks = blocks)

            BottomMenu()
        }
    }

    @Composable
    fun TopMenu( onAddBlock: (String,String) -> Unit ) {
        val items = listOf("Variables", "Conditions", "Loops", "Functions")
        val lazyListState = rememberLazyListState()
        var showDialog by remember { mutableStateOf(false) }
        var newBlockType by remember { mutableStateOf("") }

        var variables by remember { mutableStateOf<List<String>>(emptyList()) }

        LazyRow(
            state = lazyListState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState),
            modifier = Modifier
                .background(Color(5, 5, 5))
                .fillMaxWidth()
                .padding(top=45.dp)
                .height(80.dp)
                .padding(8.dp)

        ) {
            items(items.size) { index ->
                var expanded by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(120.dp, 60.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .clickable { expanded = true }
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center,

                ) {
                    Text(
                        text = items[index],
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color(15, 24, 36))
                            .width(180.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Create new var", color = Color.White) },
                            onClick = {
                                newBlockType="Variable"
                                expanded = false
                                showDialog=true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Assign", color = Color.White) },
                            onClick = {
                                onAddBlock("Assign","")
                                expanded = false
                            }
                        )
                        variables.forEach { variableName ->
                            DropdownMenuItem(
                                text = { Text(variableName, color = Color.White) },
                                onClick = {
                                    onAddBlock("Variable", variableName)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
        if (showDialog) {
            var variableName by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("New Variable") },
                text = {
                    Column {
                        Text("Enter variable name:")
                        TextField(
                            value = variableName,
                            onValueChange = { variableName = it },
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        variables = variables + variableName
                        onAddBlock("Variable", variableName)
                        showDialog = false
                    }) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

    }

    @Composable
    fun WorkArea(blocks: List<Block>) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .background(brush = Brush.radialGradient(
                    colors = listOf(Color(15, 24, 36),Color(5, 5, 5)),
                    center = Offset(500f, 500f),
                    radius = 700f
                ))
                .pointerInput(Unit) {

                },
            contentAlignment = Alignment.Center
        )
        {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {
                blocks.forEach { block ->
                    when (block.type)
                    {
                        "Assign" -> AssignmentBlock()
                        "Variable" -> InitializeVarBlock(block.varName)
                    }
                }
            }
        }
    }

    @Composable
    fun AssignmentBlock() {
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
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                        .padding(10.dp,0.dp)
                        .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
                        .background(Color(70, 106, 140), shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                )
                {
                    Text("Var",
                        color = Color.LightGray,
                        fontSize = 11.sp)
                }
                Text("=",
                    color = Color.White,
                    fontSize = 25.sp)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                        .padding(10.dp,0.dp)
                        .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
                        .background(Color(70, 106, 140), shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                )
                {
                    Text("Value or var",
                        color = Color.LightGray,
                        fontSize = 11.sp)
                }
            }

        }
    }

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
                    modifier = Modifier.padding(20.dp,0.dp,30.dp,0.dp))
                Text("$varName",
                    color = Color.White,
                    fontSize = 25.sp)
            }

        }
    }

    @Composable
    fun BottomMenu() {
        val items = listOf("Main", "Run", "Settings")
        val icons = listOf(Icons.Default.Home, Icons.Default.Star, Icons.Default.Settings)
        var selectedItem by remember { mutableStateOf(0) }

        NavigationBar(containerColor = Color(5, 5, 5)) {
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
                        indicatorColor = Color(59,160,255)
                    )
                )
            }
        }
    }


}

