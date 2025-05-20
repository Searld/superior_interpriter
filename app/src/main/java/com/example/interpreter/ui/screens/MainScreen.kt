package com.example.interpreter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.interpreter.components.BottomMenu
import com.example.interpreter.components.SidePanel
import com.example.interpreter.components.WorkArea
import com.example.interpreter.ui.components.*
import com.example.interpreter.viewmodel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val blocks = viewModel.blocks
    val selectedItem = viewModel.selectedItem

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopMenu(
                selectedItem = selectedItem,
                onItemSelected = viewModel::onItemSelected
            )
            WorkArea(blocks = blocks, viewModel)
        }
        SidePanel(viewModel)
    }
}