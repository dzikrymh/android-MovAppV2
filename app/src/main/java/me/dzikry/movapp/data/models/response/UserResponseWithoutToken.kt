package me.dzikry.movapp.data.models.response

import com.google.gson.annotations.SerializedName
import me.dzikry.movapp.data.models.User

data class UserResponseWithoutToken(
    @SerializedName("data") val data: User,
    @SerializedName("meta") val meta: Meta
)
