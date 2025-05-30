package Utils

import android.content.Context
import java.io.File
import java.io.IOException
object Configs {
    private const val FOLDER_NAME = "configs"

    private fun getOrCreateFolder(context: Context): File {
        val folder = File(context.filesDir, FOLDER_NAME)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return folder
    }

    fun export(context: Context, listOfCommands: List<String>, fileName: String) {
        try {
            val folder = getOrCreateFolder(context)
            val file = File(folder, fileName)
            file.writeText(listOfCommands.joinToString("\n"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun import(context: Context, fileName: String): List<String> {
        return try {
            val folder = getOrCreateFolder(context)
            val file = File(folder, fileName)
            file.readLines().filter { it.isNotBlank() }
        } catch (e: IOException) {
            emptyList()
        }
    }

    fun getSavedFileNames(context: Context): List<String> {
        val folder = getOrCreateFolder(context)
        return folder.listFiles()?.map { it.name } ?: emptyList()
    }
}