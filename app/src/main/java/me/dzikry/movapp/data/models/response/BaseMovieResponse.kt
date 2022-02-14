package me.dzikry.movapp.data.models.response

import com.google.gson.annotations.SerializedName

data class BaseMovieResponse(
    @SerializedName("status_code") val status_code: Int,
    @SerializedName("status_message") val status_message: String,
)
