package com.thoumar.database.todo

import androidx.room.*
@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    fun getAll(): List<Todo>

    @Query("SELECT * FROM todo WHERE tid IN (:todoIds)")
    fun loadAllByIds(todoIds: IntArray): List<Todo>

    @Query("SELECT * FROM todo WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): Todo

    @Insert
    fun insert(todo: Todo)

    @Delete
    fun delete(todo: Todo)
}