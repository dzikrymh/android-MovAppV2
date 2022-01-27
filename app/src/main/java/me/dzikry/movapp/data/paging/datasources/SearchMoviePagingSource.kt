package me.dzikry.movapp.data.paging.datasources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.data.networks.MovieAPIs
import me.dzikry.movapp.utils.Const
import okio.IOException
import retrofit2.HttpException

class SearchMoviePagingSource(private val api: MovieAPIs, private val keyword: String) : PagingSource<Int, Movie>() {
    override val keyReuseSupported: Boolean = true

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
                LoadResult.Page(
                    data = response.body()!!.results,
                    prevKey = if (page == FIRST_PAGE_INDEX) null else page - 1,
                    nextKey = if (response.body()?.total_pages!! <= page) null else page + 1
                )
            } else {
                throw IOException("Failed fetch data")
            }
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }
}