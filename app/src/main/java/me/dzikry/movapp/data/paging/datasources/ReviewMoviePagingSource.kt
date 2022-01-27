package me.dzikry.movapp.data.paging.datasources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.dzikry.movapp.data.models.Review
import me.dzikry.movapp.data.networks.MovieAPIs
import me.dzikry.movapp.utils.Const
import okio.IOException
import retrofit2.HttpException

class ReviewMoviePagingSource(private val movie_id: String) : PagingSource<Int, Review>() {

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        return try {
            val page: Int = params.key ?: FIRST_PAGE_INDEX
            val response = api.getReviewMovie(
                api_key = Const.API_KEY_MOVIE,
                page = page,
                movie_id = movie_id
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
        private val api = MovieAPIs()
    }
}