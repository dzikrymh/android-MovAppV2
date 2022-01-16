package me.dzikry.movapp.ui.movie_by_genre.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.databinding.ItemMovieBinding
import me.dzikry.movapp.utils.Const

class MovieByGenreAdapter(
    private val onMovieClick: (movie: Movie) -> Unit
) : PagingDataAdapter<Movie, MovieByGenreAdapter.MovieViewHolder>(DiffUtilCallBack()) {

    inner class MovieViewHolder(val item : ItemMovieBinding): RecyclerView.ViewHolder(item.root)

    override fun onBindViewHolder(holder: MovieByGenreAdapter.MovieViewHolder, position: Int) {
        val movie = getItem(position)!!

        Glide.with(holder.itemView.context)
            .load(Const.BASE_PATH_POSTER + movie.posterPath)
            .transform(CenterCrop())
            .into(holder.item.itemMoviePoster)

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