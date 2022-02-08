package me.dzikry.movapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.dzikry.movapp.data.models.Review
import me.dzikry.movapp.databinding.ItemReviewMovieBinding
import javax.inject.Inject

class ReviewPagingAdapter @Inject constructor() : PagingDataAdapter<Review, ReviewPagingAdapter.ReviewViewHolder>(
    DiffUtilCallBack()
) {

    var reviewClickListener: ReviewClickListener? = null

    inner class ReviewViewHolder(val item : ItemReviewMovieBinding): RecyclerView.ViewHolder(item.root) {

        private val binding = item

        fun bind(review: Review) {
            binding.review = review
            itemView.setOnClickListener {
                reviewClickListener?.onReviewClicked(binding, review)
            }
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

    interface ReviewClickListener {
        fun onReviewClicked(binding: ItemReviewMovieBinding, review: Review)
    }
}