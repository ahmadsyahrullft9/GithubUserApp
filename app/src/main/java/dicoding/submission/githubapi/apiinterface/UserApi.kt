package dicoding.submission.githubapi.apiinterface

import dicoding.submission.githubapi.models.DetailUser
import dicoding.submission.githubapi.models.SearchResponse
import dicoding.submission.githubapi.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("users")
    fun get_user(): Call<List<User>>

    @GET("search/users")
    fun search_user(
        @Query("q") search_text: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun detail_user(
        @Path("username") username: String
    ): Call<DetailUser>

    @GET("users/{username}/followers")
    fun get_followers(@Path("username") username: String): Call<List<User>>

    @GET("users/{username}/following")
    fun get_following(@Path("username") username: String): Call<List<User>>
}