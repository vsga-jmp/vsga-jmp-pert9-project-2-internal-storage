package com.example.projectvsga9internalstorage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var addNote: ImageButton
    private lateinit var listFileName: MutableList<ItemModel>
    private lateinit var itemNoteAdapter: ItemNoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        onClickListeners()
        updateFileList()
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume: Triggered")
        updateFileList()
    }

    private fun onClickListeners() {
        addNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initializeViews() {
        addNote = findViewById(R.id.btnAddNote)
    }

    private fun updateFileList() {
        listFileName = mutableListOf()
        listFileName.addAll(getListFilesInInternalStorage(this))

        itemNoteAdapter = ItemNoteAdapter(this, listFileName) { item ->
            deleteFileFromInternalStorage(this, item.fileName)
            updateFileList()
        }

        val listViewNote = findViewById<RecyclerView>(R.id.recyclerViewNote)
        listViewNote.adapter = itemNoteAdapter
        listViewNote.layoutManager = LinearLayoutManager(this)
    }

    private fun getListFilesInInternalStorage(context: Context): List<ItemModel> {
        val filesDir = context.filesDir
        val filesList = filesDir.listFiles() ?: return emptyList()

        return filesList.map { file ->
            val valueFile = readNoteFromFilename(context, file.name)
            val lastModified = file.lastModified()
            val lastModifiedDate = Date(lastModified)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(lastModifiedDate)
            ItemModel(file.name,valueFile ?: "No Content", formattedDate)
        }
    }

    private fun readNoteFromFilename(context: Context, fileName: String): String? {
        return try {
            context.openFileInput(fileName).bufferedReader().use { reader ->
                val stringBuilder = StringBuilder()
                reader.forEachLine { line ->
                    stringBuilder.append(line).append("\n")
                }
                stringBuilder.toString().trimEnd()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun deleteFileFromInternalStorage(context: Context, fileName: String) {
        val file = File(context.filesDir, fileName)

        if (file.exists()) {
            // Create and show confirmation dialog
            AlertDialog.Builder(context)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete $fileName?")
                .setPositiveButton("Yes") { dialog, _ ->
                    handleFileDeletion(file, context)
                    updateFileList()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            showToast(context, "File $fileName does not exist.")
        }
    }

    private fun handleFileDeletion(file: File, context: Context) {
        try {
            if (file.delete()) {
                showToast(context, "File deleted successfully.")
            } else {
                showToast(context, "Failed to delete file.")
            }
        } catch (e: Exception) {
            showToast(context, "Error deleting file. Please try again.")
            e.printStackTrace()
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}
