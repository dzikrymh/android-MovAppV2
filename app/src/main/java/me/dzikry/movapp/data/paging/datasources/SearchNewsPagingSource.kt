package me.dzikry.movapp.data.paging.datasources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.data.networks.NewsAPIs
import me.dzikry.movapp.utils.Const
import okio.IOException
import retrofit2.HttpException

class SearchNewsPagingSource(private val api: NewsAPIs, private val keyword: String) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val page: Int = params.key ?: FIRST_PAGE_INDEX
            val response = api.getSearchNews(
                api_key = Const.API_KEY_NEWS,
                keyword = keyword,
                language = "id",
                page = page,
                pageSize = PAGE_SIZE_NEWS,
            )
            if (response.isSuccessful) {
                LoadResult.Page(
                    data = response.body()!!.articles,
                    prevKey = if (page == FIRST_PAGE_INDEX) null else page - 1,
                    nextKey = if (response.body()?.totalResults!! < params.loadSize) null else page + 1
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
        const val PAGE_SIZE_NEWS = 20
    }
}