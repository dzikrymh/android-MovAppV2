package me.dzikry.movapp.data.models.response

import com.google.gson.annotations.SerializedName

data class SessionIDResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("session_id") val session_id: String,
)
