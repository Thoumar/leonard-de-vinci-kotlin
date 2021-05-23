package com.thoumar.leonarddevincikotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.thoumar.adapters.TodoAdapter
import com.thoumar.database.AppDatabase
import com.thoumar.database.models.Todo

class MainActivity : AppCompatActivity() {

    private var addTodoBtn: ImageButton? = null
    private var firstNameKey = "USER_FIRST_NAME"
    private var lastNameKey = "USER_LAST_NAME"
    private var appDatabase: AppDatabase? = null
    private var todoAdapter: TodoAdapter? = null
    private var todoRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get views
        val textViewWelcome = findViewById<TextView>(R.id.welcomeText)
        addTodoBtn = findViewById(R.id.add_todo)
        todoRecyclerView = findViewById(R.id.todo_rv)

        // Get first name and last name from shared preferences
        val firstName = getFirstName()
        val lastName = getLastName()

        // Set welcome sentence
        val welcomeSentence = "Bonjour $firstName $lastName"
        textViewWelcome.text = welcomeSentence

        // Functions
        loadDatabase()
        setTodoAdapter()
        setOnAddTodoClickListener()
    }

    private fun loadDatabase() {
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()
    }

    private fun setTodoAdapter() {
        todoAdapter = TodoAdapter {
            appDatabase?.todoDao()?.delete(it)
            refreshRcView()
        }
    }

    private fun setOnAddTodoClickListener () {
        addTodoBtn?.setOnClickListener {
            val title = findViewById<EditText>(R.id.editTextTodo).text.toString()
            if(title.isNotEmpty()) {
                val todo = Todo(0, title, false)
                appDatabase?.todoDao()?.insert(todo)
                refreshRcView()
                findViewById<EditText>(R.id.editTextTodo).setText("")
            }
        }
    }

    private fun getFirstName(): String? {
        return getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE).getString(
            firstNameKey,
            null
        )
    }

    private fun getLastName(): String? {
        return getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE).getString(
            lastNameKey,
            null
        )
    }

    private fun refreshRcView() {
        todoAdapter?.todoList = appDatabase?.todoDao()?.getAll()
        todoRecyclerView?.adapter = todoAdapter
        todoAdapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        refreshRcView()
        todoRecyclerView?.layoutManager = LinearLayoutManager(this)
        todoRecyclerView?.hasFixedSize()
    }
}