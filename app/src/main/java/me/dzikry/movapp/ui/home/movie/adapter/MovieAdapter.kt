package me.dzikry.movapp.ui.home.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.databinding.ItemMovieBinding
import me.dzikry.movapp.utils.Const

class MovieAdapter(
    private val onMovieClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){

    inner class MovieViewHolder(val item : ItemMovieBinding): RecyclerView.ViewHolder(item.root)

    private val differCallback = object : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.posterPath == newItem.posterPath
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,  differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]

        Glide.with(holder.itemView.context)
            .load(Const.BASE_PATH_POSTER + movie.posterPath)
            .transform(CenterCrop())
            .into(holder.item.itemMoviePoster)

        holder.itemView.setOnClickListener { onMovieClick.invoke(movie) }
    }

}