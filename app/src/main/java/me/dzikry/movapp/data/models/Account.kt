package me.dzikry.movapp.data.models

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("username") val username: String,
    @SerializedName("avatar") val avatar: Avatar,
) {
    data class Avatar(
        @SerializedName("gravatar") val gravatar: Gravatar,
        @SerializedName("tmdb") val tmdb: Tmdb?,
    )

    data class Tmdb(
        @SerializedName("avatar_path") val avatar_path: String?,
    )

    data class Gravatar(
        @SerializedName("hash") val hash: String,
    )
}
