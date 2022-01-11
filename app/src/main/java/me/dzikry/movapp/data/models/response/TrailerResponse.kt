package me.dzikry.movapp.data.models.response

import com.google.gson.annotations.SerializedName
import me.dzikry.movapp.data.models.Trailer

data class TrailerResponse(
    @SerializedName("results") val results: List<Trailer>
)
