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
                var nextPageNumber: Int? = null
                var prevPageNumber: Int? = null
                var totalPage: Int = response.body()?.totalResults?.div(PAGE_SIZE_NEWS) ?: 1
                if (totalPage == 0) totalPage = 1
                if (page != totalPage) nextPageNumber = page + 1
                if (page != FIRST_PAGE_INDEX) prevPageNumber = page - 1
                LoadResult.Page(
                    data = response.body()!!.articles,
                    prevKey = prevPageNumber,
                    nextKey = nextPageNumber
                )
            } else {
                throw IOException("Failed fetch data")
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1
        const val PAGE_SIZE_NEWS = 20
    }
}