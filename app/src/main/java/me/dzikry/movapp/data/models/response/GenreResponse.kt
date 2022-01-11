package me.dzikry.movapp.data.models.response

import com.google.gson.annotations.SerializedName
import me.dzikry.movapp.data.models.Genre

data class GenreResponse(
    @SerializedName("genres") val genres: List<Genre>
)
