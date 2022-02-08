package me.dzikry.movapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.databinding.ItemNewsTrendingBinding
import javax.inject.Inject

class TrendingAdapter @Inject constructor() : RecyclerView.Adapter<TrendingAdapter.ArticleViewHolder>(){

    var trendingClickListener: TrendingClickListener? = null

    inner class ArticleViewHolder(val item : ItemNewsTrendingBinding): RecyclerView.ViewHolder(item.root)

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,  differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return  ArticleViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_news_trending,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.item.news = article
        holder.itemView.setOnClickListener {
            trendingClickListener?.onTrendingClicked(holder.item, article)
        }
    }

    interface TrendingClickListener {
        fun onTrendingClicked(binding: ItemNewsTrendingBinding, article: Article)
    }
}