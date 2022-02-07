package me.dzikry.movapp.utils

class Const {
    companion object {
        /** API NEWS **/
        const val BASE_URL_AUTH = "http://192.168.1.6:8000/api/"

        /** API NEWS **/
        const val BASE_URL_NEWS = "https://newsapi.org/v2/"
        const val API_KEY_NEWS = "53bc8066d2c04b89b40ecaec64e20e77"

        /** API MOVIE **/
        const val BASE_URL_MOVIE = "https://api.themoviedb.org/3/"
        const val API_KEY_MOVIE = "6f444511e0cc6abeaa64e43815365c8b"

        /** Path media movie **/
        const val BASE_PATH_TRAILER = "https://www.youtube.com/watch?v="
        const val BASE_PATH_POSTER = "https://image.tmdb.org/t/p/w342"
        const val BASE_PATH_BACKDROP = "https://image.tmdb.org/t/p/w1280"
        const val BASE_PATH_AVATAR = "https://secure.gravatar.com/avatar"

        /** KEY DATA **/
        const val PREFERENCE_TOKEN = "token"
        const val TOKEN = "extra_token"
        const val USER = "extra_user"
        const val MOVIE_ID = "extra_movie_id"
        const val GENRE_ID = "extra_genre_id"
        const val GENRE_NAME = "extra_genre_name"
    }
}