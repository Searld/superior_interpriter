package com.example.interpreter

import Utils.ExceptionDialogHandler
import android.os.Build
import android.os.Bundle
import android.util.LayoutDirection
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.interpreter.components.BottomMenu
import com.example.interpreter.model.Screen
import com.example.interpreter.ui.screens.MainScreen
import com.example.interpreter.ui.screens.RunScreen
import com.example.interpreter.ui.screens.SettingsScreen
import com.example.interpreter.ui.screens.SplashScreen
import com.example.interpreter.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val viewModel: MainViewModel = viewModel()
            var showSplash by remember { mutableStateOf(true) }

            ExceptionDialogHandler()
            if(showSplash)
            {
                SplashScreen({showSplash = false})
            }
            else
            {
                Scaffold(
                    bottomBar = {
                        BottomMenu(navController = navController)
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = 0.dp,
                                bottom = 0.dp
                            )
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Main.route,
                            modifier = Modifier.fillMaxSize(),
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            composable(Screen.Main.route) {
                                MainScreen(viewModel)
                            }
                            composable(Screen.Run.route) {
                                RunScreen(viewModel)
                            }
                            composable(Screen.Settings.route) {
                                SettingsScreen()
                            }
                        }
                    }
                }
            }

        }
    }
}