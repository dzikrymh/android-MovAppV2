package me.dzikry.movapp.ui.search.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.databinding.ItemMovieBinding

class SearchMovieAdapter(
    private val onMovieClick: (movie: Movie) -> Unit
) : PagingDataAdapter<Movie, SearchMovieAdapter.MovieViewHolder>(DiffUtilCallBack()) {

    inner class MovieViewHolder(val item : ItemMovieBinding): RecyclerView.ViewHolder(item.root)

    override fun onBindViewHolder(holder: SearchMovieAdapter.MovieViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            holder.item.movie = movie
            holder.itemView.setOnClickListener { onMovieClick.invoke(movie) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchMovieAdapter.MovieViewHolder {
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