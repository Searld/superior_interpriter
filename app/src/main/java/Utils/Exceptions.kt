package Utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

object Exceptions {
    private var errorDialog: ((String) -> Unit)? = null

    fun registerDialogHandler(handler: (String) -> Unit) {
        errorDialog = handler
    }

    fun handleException(message: String) {
        errorDialog?.invoke(message)
    }

}

@Composable
fun ExceptionDialogHandler() {
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        Exceptions.registerDialogHandler { message ->
            errorMessage = message
        }
    }

    errorMessage?.let { message ->
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
            onDismissRequest = { errorMessage = null },
            title = { Text("Exception!", color = Color.White) },
            text = {
                Column {
                    Text(message, color = Color.White)
                }
            },
            confirmButton = {
                Button(onClick = {
                    errorMessage = null
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
                    Text("Ok")
                }
            }
        )
    }
}
