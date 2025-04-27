package com.example.sporty.Models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(tableName = "posts")
@Parcelize
data class Post(
    @PrimaryKey var postId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "caption") var caption: String = "",
    @ColumnInfo(name = "imageUrl") var imageUrl: String = "",
    @ColumnInfo(name = "location") var location: String = "",
    @ColumnInfo(name = "sportType") var sportType: String = "",
    @ColumnInfo(name = "sportyDate") var timestamp: String = "",
    @ColumnInfo(name = "userId") var userId: String = ""
) : Parcelable