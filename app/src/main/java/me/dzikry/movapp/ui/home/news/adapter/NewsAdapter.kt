package me.dzikry.movapp.ui.home.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.databinding.ItemNewsCategoryBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(
    private val onNewsClick: (news: Article) -> Unit
) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>(){

    inner class ArticleViewHolder(val item : ItemNewsCategoryBinding): RecyclerView.ViewHolder(item.root)

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
                R.layout.item_news_category,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.item.news = article

        Glide.with(holder.itemView.context)
            .load(article.urlToImage)
            .transform(CenterCrop())
            .into(holder.item.itemImage)

        try {
            val dateApiFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
            val date = dateApiFormat.parse(article.publishedAt)

            val dateView = SimpleDateFormat("MMM, dd yyyy", Locale.ENGLISH)

            date?.let {
                holder.item.itemPublishAuthor.text = dateView.format(it).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            holder.item.itemPublishAuthor.text = article.publishedAt
        }

        holder.itemView.setOnClickListener { onNewsClick.invoke(article) }
    }

}