package me.dzikry.movapp.ui.search.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.databinding.ItemNewsTrendingBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SearchNewsAdapter(
    private val onNewsClick: (article: Article) -> Unit
) : PagingDataAdapter<Article, SearchNewsAdapter.NewsViewHolder>(DiffUtilCallBack()) {

    inner class NewsViewHolder(val item : ItemNewsTrendingBinding): RecyclerView.ViewHolder(item.root) {
        private val binding = item

        fun bind(article: Article) {
            binding.news = article

            Glide.with(itemView.context)
                .load(article.urlToImage)
                .transform(CenterCrop())
                .into(binding.itemImage)

            try {
                val dateApiFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
                val date = dateApiFormat.parse(article.publishedAt)

                val dateView = SimpleDateFormat("MMM, dd yyyy", Locale.ENGLISH)

                date?.let {
                    binding.itemPublish.text = dateView.format(it).toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                binding.itemPublish.text = article.publishedAt
            }

            itemView.setOnClickListener { onNewsClick.invoke(article) }
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = getItem(position)!!
        holder.bind(article)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsViewHolder {
        return NewsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_news_trending,
                parent,
                false
            )
        )
    }

    class DiffUtilCallBack: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}