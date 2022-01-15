package me.dzikry.movapp.ui.home.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import me.dzikry.movapp.data.models.Genre
import me.dzikry.movapp.databinding.ItemGenreMovieBinding
import me.dzikry.movapp.databinding.ItemMovieBinding
import me.dzikry.movapp.utils.Const

class GenreAdapter(
    private val onGenreClick: (movie: Genre) -> Unit
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>(){

    inner class GenreViewHolder(val item : ItemGenreMovieBinding): RecyclerView.ViewHolder(item.root)

    private val differCallback = object : DiffUtil.ItemCallback<Genre>(){
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,  differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            ItemGenreMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = differ.currentList[position]

        holder.item.genre.text = genre.name
        holder.itemView.setOnClickListener { onGenreClick.invoke(genre) }
    }

}