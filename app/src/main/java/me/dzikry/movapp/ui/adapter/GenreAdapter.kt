package me.dzikry.movapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.dzikry.movapp.data.models.Genre
import me.dzikry.movapp.databinding.ItemGenreMovieBinding
import javax.inject.Inject

class GenreAdapter @Inject constructor() : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>(){

    var genreClickListener: GenreClickListener? = null

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
        holder.item.genre = genre
        holder.itemView.setOnClickListener {
            genreClickListener?.onGenreClicked(holder.item, genre)
        }
    }

    interface GenreClickListener {
        fun onGenreClicked(binding: ItemGenreMovieBinding, genre: Genre)
    }

}