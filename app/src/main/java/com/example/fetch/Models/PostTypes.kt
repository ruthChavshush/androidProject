package com.example.sporty.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PostTypes : Parcelable {
    SINGLE, PLAYDATE, OTHER
}
