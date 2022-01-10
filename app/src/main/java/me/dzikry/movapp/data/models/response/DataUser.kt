package me.dzikry.movapp.data.models.response

import com.google.gson.annotations.SerializedName
import me.dzikry.movapp.data.models.User

data class DataUser(
    @SerializedName("access_token") val access_token: String,
    @SerializedName("token_type") val token_type: String,
    @SerializedName("user") val user: User
)