package me.dzikry.movapp.data.models.response

import com.google.gson.annotations.SerializedName
import me.dzikry.movapp.data.models.Review

data class ReviewResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<Review>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("total_results") val total_results: Int,
)
