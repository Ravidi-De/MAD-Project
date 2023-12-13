package com.app.newz.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.newz.utils.Constants.POST_CONTENT_COLUMN
import com.app.newz.utils.Constants.POST_TABLE
import com.app.newz.utils.Constants.POST_TITLE_COLUMN

@Entity(tableName = POST_TABLE)
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val postId :Int,

    @ColumnInfo(name = POST_TITLE_COLUMN)
    val postTitle :String,

    @ColumnInfo(name = POST_CONTENT_COLUMN)
    val postContent :String
)
