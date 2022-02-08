package me.dzikry.movapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Genre
import me.dzikry.movapp.databinding.ItemGenreDetailMovieBinding
import javax.inject.Inject

class GenreDetailAdapter @Inject constructor(
    private var genres: MutableList<Genre>,
) : RecyclerView.Adapter<GenreDetailAdapter.GenreViewHolder>() {

    var genreDetailClickListener: GenreDetailClickListener? = null

    inner class GenreViewHolder(val item: ItemGenreDetailMovieBinding) : RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenreViewHolder {
        return GenreViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_genre_detail_movie,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres[position]
        holder.item.genre = genre
        holder.itemView.setOnClickListener {
            genreDetailClickListener?.onGenreDetailClicked(holder.item, genre)
        }
    }

    override fun getItemCount(): Int = genres.size

    fun appendGenres(genres: List<Genre>) {
        this.genres.addAll(genres)
        notifyItemRangeInserted(
            this.genres.size,
            genres.size - 1
        )
        notifyDataSetChanged()
    }

    interface GenreDetailClickListener {
        fun onGenreDetailClicked(binding: ItemGenreDetailMovieBinding, genre: Genre)
    }

}