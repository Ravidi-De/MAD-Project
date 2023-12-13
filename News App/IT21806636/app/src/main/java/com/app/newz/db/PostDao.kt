package com.app.newz.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.newz.utils.Constants.POST_TABLE

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(postEntity: PostEntity)

    @Update
    fun updatePost(postEntity: PostEntity)

    @Delete
    fun deletePost(postEntity: PostEntity)

    @Query("SELECT * FROM $POST_TABLE ORDER BY postId DESC")
    fun getAllPosts() : MutableList<PostEntity>

    @Query("SELECT * FROM $POST_TABLE WHERE postId = :id")
    fun getPost(id: Int) : PostEntity
}