package com.example.sporty.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sporty.Models.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    // todo remove postType change function name
    @Query("SELECT * FROM posts")
    fun getPostsByPostType(): List<Post>
}