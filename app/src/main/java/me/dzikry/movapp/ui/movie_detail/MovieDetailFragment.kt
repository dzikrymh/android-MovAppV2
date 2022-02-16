package me.dzikry.movapp.ui.movie_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Genre
import me.dzikry.movapp.data.models.Review
import me.dzikry.movapp.databinding.FragmentMovieDetailBinding
import me.dzikry.movapp.databinding.ItemGenreDetailMovieBinding
import me.dzikry.movapp.databinding.ItemReviewMovieBinding
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.ui.adapter.GenreDetailAdapter
import me.dzikry.movapp.ui.adapter.ReviewPagingAdapter
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.PagingLoadStateAdapter
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools
import javax.inject.Inject
import kotlin.Exception
import kotlin.properties.Delegates

@AndroidEntryPoint
class MovieDetailFragment : Fragment(), GenreDetailAdapter.GenreDetailClickListener,
    ReviewPagingAdapter.ReviewClickListener {

    companion object {
        fun newInstance() = MovieDetailFragment()
    }

    private val viewModel: MovieDetailViewModel by viewModels()
    private lateinit var binding: FragmentMovieDetailBinding
    private var isFavorite by Delegates.notNull<Boolean>()

    @Inject lateinit var genreDetailAdapter: GenreDetailAdapter
    @Inject lateinit var reviewPagingAdapter: ReviewPagingAdapter

    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        Tools.setStatusBarTransparent(requireActivity())
        Tools.setMargins(binding.btnFavorite, 0, Tools.getStatusBarHeight(requireContext()) + Tools.dpToPx(requireContext(), 16), Tools.dpToPx(requireContext(), 16), 0)
        Tools.setMargins(binding.btnBack, Tools.dpToPx(requireContext(), 16), Tools.getStatusBarHeight(requireContext()) + Tools.dpToPx(requireContext(), 16), 0, 0)
        HomeActivity.animate(true)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getAccountState(
            session_id = args.sessionId,
            movie_id = args.movieId.toString()
        )
        viewModel.accountState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        binding.apply {
                            isFavorite = it.favorite
                            if (it.favorite) {
                                favorite.setImageDrawable(context?.getDrawable(R.drawable.ic_favorite))
                            } else {
                                favorite.setImageDrawable(context?.getDrawable(R.drawable.ic_favorite_placeholder))
                            }
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
        }

        binding.btnFavorite.setOnClickListener {
            viewModel.markAsFavorite(
                account_id = args.accountId,
                session_id = args.sessionId,
                movie_id = args.movieId.toString(),
                fav = !isFavorite,
            )
            viewModel.markAsFavorite.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        isFavorite = !isFavorite
                        binding.apply {
                            if (isFavorite) {
                                favorite.setImageDrawable(context?.getDrawable(R.drawable.ic_favorite))
                                Toast.makeText(context, "Movie added successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                favorite.setImageDrawable(context?.getDrawable(R.drawable.ic_favorite_placeholder))
                                Toast.makeText(context, "Movie deleted successfully", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Resource.Loading -> {

                    }
                }
            }
        }

        viewModel.getMovie(args.movieId.toString())
        viewModel.movie.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        with(genreDetailAdapter) {
                            binding.apply {
                                movie = it
                                genreMovie.apply {
                                    adapter = this@with
                                }
                            }
                            appendGenres(it.genres)
                            genreDetailClickListener = this@MovieDetailFragment
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
        }

        viewModel.getTrailer(args.movieId.toString())
        binding.movieTrailer.setOnClickListener {
            viewModel.trailer.observe(viewLifecycleOwner) { response ->
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
            }
        }

        initReviewsSize()
        reviewPagingAdapter.apply {
            binding.reviewMovies.adapter = withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter(this),
                footer = PagingLoadStateAdapter(this)
            )
            reviewClickListener = this@MovieDetailFragment
        }
        lifecycleScope.launch {
            viewModel.getReviewPaging(args.movieId.toString())
        }
        lifecycleScope.launch {
            viewModel.reviewFlow.collectLatest {
                reviewPagingAdapter.submitData(it)
            }
        }

    }

    private fun showReviewDetail(review: Review) {
        Toast.makeText(context, review.author, Toast.LENGTH_SHORT).show()
    }

    private fun showMovieByGenre(genre: Genre) {
        val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToMovieByGenreFragment(genre.id, args.accountId, args.sessionId)
        findNavController().navigate(action)
    }

    private fun initReviewsSize() {
        val param = binding.reviewMovies.layoutParams
        param.width = Tools.getWidthScreen(requireActivity())
        param.height = Tools.getHeightScreen(requireActivity()) - Tools.getStatusBarHeight(requireContext()) - Tools.getSizeBottomNavBar(requireContext())
        binding.reviewMovies.layoutParams = param
    }

    override fun onGenreDetailClicked(binding: ItemGenreDetailMovieBinding, genre: Genre) {
        showMovieByGenre(genre)
    }

    override fun onReviewClicked(binding: ItemReviewMovieBinding, review: Review) {
        showReviewDetail(review)
    }

}