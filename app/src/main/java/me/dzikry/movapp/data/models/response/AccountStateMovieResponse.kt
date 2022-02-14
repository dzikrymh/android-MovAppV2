package me.dzikry.movapp.data.models.response

import com.google.gson.annotations.SerializedName

data class AccountStateMovieResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("favorite") val favorite: Boolean,
    @SerializedName("rated") val rated: Boolean,
    @SerializedName("watchlist") val watchlist: Boolean,
)
