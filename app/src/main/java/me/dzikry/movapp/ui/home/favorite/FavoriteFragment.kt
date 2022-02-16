package me.dzikry.movapp.ui.home.favorite

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.dzikry.movapp.data.models.Account
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.databinding.FragmentFavoriteBinding
import me.dzikry.movapp.databinding.ItemMovieBinding
import me.dzikry.movapp.ui.adapter.MoviePagingAdapter
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.PagingLoadStateAdapter
import me.dzikry.movapp.utils.SpacingItemDecoration
import me.dzikry.movapp.utils.Tools
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment(), MoviePagingAdapter.MovieClickListener {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var binding: FragmentFavoriteBinding

    @Inject lateinit var mAdapter: MoviePagingAdapter
    private lateinit var moviesLayoutMgr: StaggeredGridLayoutManager

    private lateinit var sessionid: String
    private lateinit var account: Account

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val bundle: Bundle? = activity?.intent?.extras
        bundle?.let {
            it.apply {
                sessionid = getString(Const.SESSION_ID)!!
                val jsonAccount = getString(Const.ACCOUNT)
                val gson = Gson()
                account = gson.fromJson(jsonAccount, Account::class.java)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        Tools.setStatusBarTransparent(requireActivity())
        Tools.setMargins(
            binding.headerContainer,
            left = 0,
            top = Tools.getStatusBarHeight(requireContext()),
            right = 0,
            bottom = 0
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        moviesLayoutMgr = StaggeredGridLayoutManager(
            Tools.getGridSpanCountMovie(requireActivity()),
            StaggeredGridLayoutManager.VERTICAL
        )
        with(mAdapter) {
            binding.apply {
                favoriteMovies.layoutManager = StaggeredGridLayoutManager(Tools.getGridSpanCountMovie(requireActivity()), StaggeredGridLayoutManager.VERTICAL)
                val decoration = SpacingItemDecoration(Tools.getGridSpanCountMovie(requireActivity()), Tools.dpToPx(requireContext(), 2), false)
                favoriteMovies.addItemDecoration(decoration)
                favoriteMovies.adapter = withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(this@with),
                    footer = PagingLoadStateAdapter(this@with)
                )
            }
            movieClickListener = this@FavoriteFragment
        }

        lifecycleScope.launch {
            viewModel.getDiscoverFavoriteMovie(
                account_id = account.id,
                session_id = sessionid,
            )
        }
        lifecycleScope.launch {
            viewModel.movieFavoriteFlow.collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    private fun showDetailMovie(movie: Movie) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToMovieDetailFragment(movie.id, account.id, sessionid)
        findNavController().navigate(action)
    }

    override fun onMovieClicked(binding: ItemMovieBinding, movie: Movie) {
        showDetailMovie(movie)
    }

}