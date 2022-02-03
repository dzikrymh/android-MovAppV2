package me.dzikry.movapp.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("roles") val roles: String,
    @SerializedName("profile_photo_url") val profile_photo_url: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
)