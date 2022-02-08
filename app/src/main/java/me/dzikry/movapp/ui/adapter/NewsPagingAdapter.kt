package me.dzikry.movapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.databinding.ItemNewsTrendingBinding
import javax.inject.Inject

class NewsPagingAdapter @Inject constructor() : PagingDataAdapter<Article, NewsPagingAdapter.NewsViewHolder>(
    DiffUtilCallBack()
) {

    var newsClickListener: NewsClickListener? = null

    inner class NewsViewHolder(val item : ItemNewsTrendingBinding): RecyclerView.ViewHolder(item.root) {
        private val binding = item

        fun bind(article: Article) {
            binding.news = article
            itemView.setOnClickListener {
                newsClickListener?.onNewsClicked(binding, article)
            }
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

    interface NewsClickListener {
        fun onNewsClicked(binding: ItemNewsTrendingBinding, article: Article)
    }
}