package com.thoumar.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.thoumar.database.todo.Todo
import com.thoumar.leonarddevincikotlin.R
import java.util.ArrayList

class TodoAdapter(
    var todoList: List<Todo>? = ArrayList<Todo>(),
    var removeClicked: (todo: Todo) -> Unit
): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){

    private var onTodoDeleteClickedListener: OnTodoDeleteClickedListener?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val layout = if (itemCount == 0) R.layout.empty_view else R.layout.todo_item_view
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TodoViewHolder(view, todoList!!)
    }

    override fun getItemCount(): Int {
        return if(todoList!!.isEmpty()) 0 else todoList!!.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int){
        holder.view.findViewById<Button>(R.id.deleteBtn).setOnClickListener {
            Log.d("[REMOVE]", "delete clicked")
            removeClicked(todoList!![position])
//            onTodoDeleteClickedListener!!.onTodoDeleteClicked(todoList!![position])
        }
        holder.onBindViews(position)
    }

    inner class TodoViewHolder(val view: View, val todoList: List<Todo>): RecyclerView.ViewHolder(view){
        fun onBindViews(position: Int){
            if (itemCount != 0){
                view.findViewById<TextView>(R.id.title).text = todoList.get(position).title
            }

        }
    }

    fun setTodoItemClickedListener(onTodoItemClickedListener: OnTodoDeleteClickedListener){
        this.onTodoDeleteClickedListener = onTodoDeleteClickedListener
    }

    interface OnTodoDeleteClickedListener {
        fun onTodoDeleteClicked(todo: Todo)
    }
}