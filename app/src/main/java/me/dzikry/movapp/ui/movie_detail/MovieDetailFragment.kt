package me.dzikry.movapp.ui.movie_detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import me.dzikry.movapp.data.models.Genre
import me.dzikry.movapp.data.models.Review
import me.dzikry.movapp.data.networks.MovieAPIs
import me.dzikry.movapp.data.repositories.MovieRepository
import me.dzikry.movapp.databinding.FragmentMovieDetailBinding
import me.dzikry.movapp.ui.movie_detail.adapter.GenreAdapter
import me.dzikry.movapp.ui.movie_detail.adapter.ReviewAdapter
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools
import java.text.SimpleDateFormat
import kotlin.Exception

class MovieDetailFragment : Fragment() {

    companion object {
        fun newInstance() = MovieDetailFragment()
    }

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var binding: FragmentMovieDetailBinding

    private lateinit var genreAdapter: GenreAdapter
    private lateinit var reviewAdapter: ReviewAdapter

    private val args: MovieDetailFragmentArgs by navArgs()
    private var reviewPage = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val api = MovieAPIs()
        val repo = MovieRepository(api = api)
        val factory = MovieDetailViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[MovieDetailViewModel::class.java]

        viewModel.getMovie(args.movieId.toString())
        viewModel.getTrailer(args.movieId.toString())
        viewModel.getReviews(args.movieId.toString(), reviewPage)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        genreAdapter = GenreAdapter(mutableListOf()) { genre -> showMovieByGenre(genre) }
        reviewAdapter = ReviewAdapter(mutableListOf()) { review -> showReviewDetail(review) }

        viewModel.movie.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        binding.apply {
                            Glide.with(requireActivity())
                                .load(Const.BASE_PATH_BACKDROP + it.backdropPath)
                                .transform(CenterCrop())
                                .into(movieBackdrop)

                            Glide.with(requireActivity())
                                .load(Const.BASE_PATH_POSTER + it.posterPath)
                                .transform(CenterCrop())
                                .into(moviePoster)

                            movieTitle.text = it.title
                            movieRating.rating = it.rating / 2
                            try {
                                val parser = SimpleDateFormat("yyyy-MM-dd")
                                val formatter = SimpleDateFormat("dd MMMM yyyy")
                                val dt = formatter.format(parser.parse(it.releaseDate))
                                movieReleaseDate.text = dt
                            } catch (e: Exception) {
                                movieReleaseDate.text = it.releaseDate
                            }
                            movieOverview.text = it.overview

                            binding.genreMovie.apply {
                                adapter = genreAdapter
                            }
                            genreAdapter.appendGenres(it.genres)
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {

                }
            }
        })

        binding.movieTrailer.setOnClickListener {
            viewModel.trailer.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.movieTrailer.visibility = View.VISIBLE
                        response.data?.let {
                            try {
                                val url = Const.BASE_PATH_TRAILER + it[0].key
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(url)
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Trailer tidak ditemukan",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding.movieTrailer.visibility = View.VISIBLE
                        response.message?.let {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Resource.Loading -> {
                        binding.movieTrailer.visibility = View.GONE
                    }
                }
            })
        }

        initReviewsSize()
        viewModel.reviews.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        binding.reviewMovies.apply {
                            adapter = reviewAdapter
                        }
                        reviewAdapter.appendReviews(it)
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {

                }
            }
        })
    }

    private fun showReviewDetail(review: Review) {
        Toast.makeText(context, review.author, Toast.LENGTH_SHORT).show()
    }

    private fun showMovieByGenre(genre: Genre) {
        val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToMovieByGenreFragment(genre.id)
        findNavController().navigate(action)
    }

    private fun initReviewsSize() {
        val param = binding.reviewMovies.layoutParams
        param.width = Tools.getWidthScreen(requireActivity())
        param.height = Tools.getHeightScreen(requireActivity())
        binding.reviewMovies.layoutParams = param
    }

}