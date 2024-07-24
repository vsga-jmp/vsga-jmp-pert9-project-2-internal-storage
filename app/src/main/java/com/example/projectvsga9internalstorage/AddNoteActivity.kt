package com.example.projectvsga9internalstorage

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddNoteActivity : ComponentActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnSaved: Button
    private lateinit var filenameEditText: EditText
    private lateinit var contentEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_add_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setOnClickListeners()
    }

    private fun initializeViews() {
        btnSaved = findViewById(R.id.btnSaved)
        btnBack = findViewById(R.id.btnBack)
        filenameEditText = findViewById(R.id.filenameEditText)
        contentEditText = findViewById(R.id.contentEditText)
    }

    private fun setOnClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnSaved.setOnClickListener {
            writeTextToFile(this, filenameEditText.text.toString(), contentEditText.text.toString())
            finish()
        }
    }

    private fun writeTextToFile(context: Context, filename: String, content: String) {
        val file = File(context.filesDir, filename)
        try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(content.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
