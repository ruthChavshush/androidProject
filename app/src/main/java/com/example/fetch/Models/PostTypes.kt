package com.example.fetch.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PostTypes : Parcelable {
    SINGLE, PLAYDATE, OTHER
}
