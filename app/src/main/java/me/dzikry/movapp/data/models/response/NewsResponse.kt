package me.dzikry.movapp.data.models.response

import com.google.gson.annotations.SerializedName
import me.dzikry.movapp.data.models.Article

data class NewsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<Article>,
)
