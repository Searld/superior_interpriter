package com.example.interpreter.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.interpreter.model.Block
import com.example.interpreter.R
import com.example.interpreter.model.Value
import com.example.interpreter.model.Variable
import com.example.interpreter.viewmodel.MainViewModel

@Composable
fun WhileBlock(
    block: Block.WhileBlock,
    viewModel: MainViewModel
) {
    val selectedSlot = viewModel.selectedSlot
    val isLeftSelected = selectedSlot?.blockId == block.id && selectedSlot.slot == "left"
    val isRightSelected = selectedSlot?.blockId == block.id && selectedSlot.slot == "right"
    val value = remember{mutableStateOf("")}
    var showDialog by remember { mutableStateOf(false) }

    var selected by remember { mutableStateOf(">") }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(">", "<", ">=", "<=", "==", "!=")
    block.operator = selected
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
                color = Color(59, 160, 255).copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    )  {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            Text("While", color = Color.White, fontSize = 20.sp,
                modifier = Modifier.padding(10.dp,0.dp),
                fontFamily = FontFamily(Font(R.font.lato)))

            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .height(30.dp)
                    .padding(end = 10.dp)
                    .border(
                        width = 1.dp,
                        color = if (isLeftSelected) Color.Red.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color(70, 106, 140).copy(alpha = 0.09f), shape = RoundedCornerShape(8.dp))
                    .clickable {
                        if (isLeftSelected) viewModel.clearSelectedSlot()
                        else {
                            viewModel.selectSlot(block.id, "left")
                            showDialog = true
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                val text = when (val left = block.leftExpr) {
                    is Variable -> left.name
                    is Value -> left.value
                    else -> "Expression"
                }
                Text(text, color = Color.LightGray, fontSize = 11.sp)
            }
            Box {
                Text(
                    text = selected,
                    color = Color.White,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .clickable { expanded = true }
                        .padding(8.dp)
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(Color(15, 24, 36))
                        .width(50.dp)
                ) {
                    options.forEach { operator ->
                        DropdownMenuItem(
                            text = { Text(operator, color = Color.White) },
                            onClick = {
                                selected = operator
                                expanded = false
                            }
                        )
                    }

                }
            }


            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .height(30.dp)
                    .padding(10.dp, 0.dp)
                    .border(
                        width = 1.dp,
                        color = if (isRightSelected) Color.Red.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color(70, 106, 140).copy(alpha = 0.09f), shape = RoundedCornerShape(8.dp))
                    .clickable {
                        if (isRightSelected) viewModel.clearSelectedSlot()
                        else
                        {
                            viewModel.selectSlot(block.id, "right")
                            showDialog = true
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                val text = when (val right = block.rightExpr) {
                    is Variable -> right.name
                    is Value -> right.value
                    else -> "Expression"
                }
                Text(text, color = Color.LightGray, fontSize = 11.sp)
            }
        }
        if(showDialog)
        {
            var value by remember { mutableStateOf("") }

            AlertDialog(
                modifier = Modifier.border(
                    BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(25.dp)
                ),
                containerColor =Color(59, 160, 255).copy(alpha = 0.1f),
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = { showDialog = false },
                title = { Text("Create value", color = Color.White) },
                text = {
                    Column {
                        Text("Enter value:", color = Color.White)
                        TextField(
                            value = value,
                            onValueChange = { value = it },
                            singleLine = true,
                            modifier = Modifier.padding(0.dp,10.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(70, 106, 140).copy(alpha = 0.09f),
                                unfocusedContainerColor = Color(70, 106, 140).copy(alpha = 0.09f),
                                focusedIndicatorColor = Color.White,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.insertIntoSelectedSlot(Value(value))
                        showDialog = false
                    },
                        modifier = Modifier.background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(59, 160, 255),
                                    Color(121, 59, 255)
                                )
                            ),
                            shape = RoundedCornerShape(40.dp)
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        if(isRightSelected) block.rightExpr = null
                        if(isLeftSelected) block.leftExpr = null
                        showDialog = false },
                        modifier = Modifier.border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(40.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}