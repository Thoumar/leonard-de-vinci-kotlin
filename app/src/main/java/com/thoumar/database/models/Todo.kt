package com.thoumar.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var tId: Int = 0,

    @ColumnInfo(name = "title")
    var title: String? = "",

    @ColumnInfo(name = "checked")
    var checked: Boolean? = false
)