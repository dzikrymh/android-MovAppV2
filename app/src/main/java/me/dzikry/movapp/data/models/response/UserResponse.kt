package me.dzikry.movapp.data.models.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("data") val data: DataUser,
    @SerializedName("meta") val meta: Meta
)