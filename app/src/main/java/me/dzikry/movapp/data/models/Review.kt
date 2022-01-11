package me.dzikry.movapp.data.models

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("author") val author: String,
    @SerializedName("author_details") val author_details: Author,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
)
