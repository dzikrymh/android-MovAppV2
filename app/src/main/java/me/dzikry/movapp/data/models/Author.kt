package me.dzikry.movapp.data.models

import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("avatar_path") val photo: String,
    @SerializedName("rating") val rating: Double,
)
