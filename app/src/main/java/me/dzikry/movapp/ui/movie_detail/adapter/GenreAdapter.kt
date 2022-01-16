package me.dzikry.movapp.ui.movie_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Genre
import me.dzikry.movapp.databinding.ItemGenreDetailMovieBinding

class GenreAdapter(
    private var genres: MutableList<Genre>,
    private val onGenreClick: (genre: Genre) -> Unit
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

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
        holder.itemView.setOnClickListener { onGenreClick.invoke(genre) }
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

}