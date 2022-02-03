package me.dzikry.movapp.ui.movie_by_genre.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.databinding.ItemMovieBinding

class MovieByGenreAdapter(
    private val onMovieClick: (movie: Movie) -> Unit
) : PagingDataAdapter<Movie, MovieByGenreAdapter.MovieViewHolder>(DiffUtilCallBack()) {

    inner class MovieViewHolder(val item : ItemMovieBinding): RecyclerView.ViewHolder(item.root)

    override fun onBindViewHolder(holder: MovieByGenreAdapter.MovieViewHolder, position: Int) {
        val movie = getItem(position)!!
        holder.item.movie = movie
        holder.itemView.setOnClickListener { onMovieClick.invoke(movie) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieByGenreAdapter.MovieViewHolder {
        return MovieViewHolder(
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    class DiffUtilCallBack: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.posterPath == newItem.posterPath
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}