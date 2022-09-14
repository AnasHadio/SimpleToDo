package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove item from list
                listOfTasks.removeAt(position)

                // Notify adapter of list change
                adapter.notifyDataSetChanged()

                // Save after adding
                saveItems()
            }
        }

        // Get our Items to display
        loadItems()

        // Creating Variable for inputted text
        val inputtedText = findViewById<EditText>(R.id.AddJob)

        // Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewJobs)

        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        // Attach adapter to recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up button and input field to enter a Job
        findViewById<Button>(R.id.addButton).setOnClickListener{
            // Grab text that is inputted
            val inputtedJob = inputtedText.text.toString()

            // Add string to list of Jobs
            listOfTasks.add(inputtedJob)

            // Notify adapter of change to list of jobs
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // Clear field for new task
            inputtedText.setText("")

            // Save after adding
            saveItems()
        }
    }


    // Get the file we need
    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save the items by writing to a file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }


}