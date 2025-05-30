package com.example.interpreter.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.interpreter.components.BottomMenu
import com.example.interpreter.components.SidePanelVariables
import com.example.interpreter.components.SidePanelConditions
import com.example.interpreter.components.SidePanelFunctions
import com.example.interpreter.components.SidePanelLoops
import com.example.interpreter.components.WorkArea
import com.example.interpreter.ui.components.*
import com.example.interpreter.viewmodel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val blocks = viewModel.blocks
    val selectedItem = viewModel.selectedItem

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()
            .background(brush = Brush.radialGradient(
            colors = listOf(Color(15, 24, 36),Color(5, 5, 5)),
            center = Offset(500f, 1000f),
            radius = 700f
        ))) {
            TopMenu(
                selectedItem = selectedItem,
                onItemSelected = viewModel::onItemSelected
            )
            WorkArea(blocks = blocks, viewModel)
        }
        SidePanelVariables(viewModel)
        SidePanelConditions(viewModel)
        SidePanelFunctions(viewModel)
        SidePanelLoops(viewModel)
    }
}