package me.dzikry.movapp.ui.search.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.data.networks.MovieAPIs
import me.dzikry.movapp.utils.Const
import okio.IOException

class SearchMoviePagingSource(private val api: MovieAPIs, private val keyword: String) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page: Int = params.key ?: FIRST_PAGE_INDEX
            val response = api.getSearchMovie(
                api_key = Const.API_KEY_MOVIE,
                page = page,
                query = keyword
            )
            if (response.isSuccessful) {
                var nextPageNumber: Int? = null
                var prevPageNumber: Int? = null
                if (page != response.body()?.total_pages) nextPageNumber = page + 1 else null
                if (page != FIRST_PAGE_INDEX) prevPageNumber = page - 1 else null
                LoadResult.Page(
                    data = response.body()!!.results,
                    prevKey = prevPageNumber,
                    nextKey = nextPageNumber
                )
            } else {
                throw IOException("Gagal mengambil data")
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }
}