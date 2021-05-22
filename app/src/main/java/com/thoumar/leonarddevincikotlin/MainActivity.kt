package com.thoumar.leonarddevincikotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.thoumar.adapters.TodoAdapter
import com.thoumar.database.AppDatabase
import com.thoumar.database.todo.Todo

class MainActivity : AppCompatActivity() {

    private var appDatabase: AppDatabase? = null
    private var todoAdapter: TodoAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val allSP = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE).all
        Log.d("[MainActivity]", allSP.toString())


        val prenom = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE).getString(
            "USER_FIRST_NAME",
            null
        )
        val nom = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE).getString(
            "USER_LAST_NAME",
            null
        )

        Toast.makeText(this, "$prenom $nom", Toast.LENGTH_LONG).show()

        findViewById<TextView>(R.id.welcomeText).text = "Bonjour ${nom} ${prenom}"

         appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()

        todoAdapter = TodoAdapter {
            appDatabase?.todoDao()?.delete(it)
            refreshRcView()
        }

        findViewById<Button>(R.id.add_todo).setOnClickListener {
            val title = findViewById<EditText>(R.id.editTextTodo).text.toString()

            if(!title.isNullOrEmpty()) {
                val todo = Todo(0, title, false)
                appDatabase?.todoDao()?.insert(todo)
                refreshRcView()
            }
        }
    }

    private fun refreshRcView() {
        todoAdapter?.todoList = appDatabase?.todoDao()?.getAll()
        findViewById<RecyclerView>(R.id.todo_rv).adapter = todoAdapter
        todoAdapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        refreshRcView()
        findViewById<RecyclerView>(R.id.todo_rv).layoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.todo_rv).hasFixedSize()
    }

}