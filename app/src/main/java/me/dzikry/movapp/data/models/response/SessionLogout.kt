package me.dzikry.movapp.data.models.response

import com.google.gson.annotations.SerializedName

data class SessionLogout(
    @SerializedName("success") val success: Boolean
)
