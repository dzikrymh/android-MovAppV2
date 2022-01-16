package me.dzikry.movapp.ui.movie_detail.adapter

import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Review
import me.dzikry.movapp.databinding.ItemReviewMovieBinding
import me.dzikry.movapp.utils.Const
import java.lang.Exception
import java.lang.StringBuilder
import java.text.SimpleDateFormat

class ReviewAdapter(
    private var reviews: MutableList<Review>,
    private var onReviewClick: (review: Review) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = ItemReviewMovieBinding.inflate(view, parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size

    fun appendReviews(reviews: List<Review>) {
        this.reviews.addAll(reviews)
        notifyItemRangeInserted(
            this.reviews.size,
            reviews.size - 1
        )
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(itemView: ItemReviewMovieBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView

        fun bind(review: Review) {
            if (review.author_details.photo != null) {
                val url = StringBuilder(review.author_details.photo)
                if (url.indexOf("http") == -1) {
                    url.insert(0, Const.BASE_PATH_AVATAR)
                } else {
                    url.deleteCharAt(0)
                }
                Glide.with(itemView.context)
                    .load(url.toString())
                    .transform(CenterCrop())
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.profileImage.setImageResource(R.drawable.ic_account)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .into(binding.profileImage)
            }

            binding.author.text = review.author
            try {
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val formatter = SimpleDateFormat("dd MMMM yyyy")
                val dt = formatter.format(parser.parse(review.updated_at))
                binding.date.text = dt
            } catch (e: Exception) {
                binding.date.text = review.updated_at
            }
            if (review.author_details.rating != null) {
                binding.reviewRate.text = review.author_details.rating.toString()
            } else {
                binding.reviewRate.text = "0.0"
            }
            binding.content.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(review.content, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(review.content)
            }

            itemView.setOnClickListener { onReviewClick.invoke(review) }
        }
    }
}