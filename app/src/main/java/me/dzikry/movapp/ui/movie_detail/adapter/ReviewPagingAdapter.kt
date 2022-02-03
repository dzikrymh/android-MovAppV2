package me.dzikry.movapp.ui.movie_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.dzikry.movapp.data.models.Review
import me.dzikry.movapp.databinding.ItemReviewMovieBinding

class ReviewPagingAdapter(
    private val onReviewClick: (review: Review) -> Unit
) : PagingDataAdapter<Review, ReviewPagingAdapter.ReviewViewHolder>(DiffUtilCallBack()) {

    inner class ReviewViewHolder(val item : ItemReviewMovieBinding): RecyclerView.ViewHolder(item.root) {

        private val binding = item

        fun bind(review: Review) {
            binding.review = review
            itemView.setOnClickListener { onReviewClick.invoke(review) }
        }
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = getItem(position)!!
        holder.bind(review)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            ItemReviewMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    class DiffUtilCallBack: DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.author == newItem.author
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
}