package com.thoumar.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.thoumar.database.todo.Todo
import com.thoumar.database.todo.TodoDao

@Database(
    version = 1,
    entities = [Todo::class]
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}