package com.edlo.mydemoapp.repository.net.github

import com.edlo.mydemoapp.repository.net.github.data.GitHubBaseResponse
import com.edlo.mydemoapp.repository.net.github.data.GithubUserData
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object ApiGitHub {
    const val BASE_URL = "https://api.github.com/"

    const val GET_SEARCH_USERS = "search/users"
    const val GET_USERS = "users/{user}"
}

interface ApiGitHubService {

    @GET(ApiGitHub.GET_SEARCH_USERS)
    fun listUsers( @Query("q") q: String,
        @Query("sort") sort: String?, @Query("order") order: String?,
        @Query("per_page") perPage: Int?, @Query("page") page: Int?,
    ): Deferred<GitHubBaseResponse<List<GithubUserData>>>?

    @GET(ApiGitHub.GET_USERS)
    fun getUserDatails(@Path("user") login: String,
    ): Deferred<GithubUserData>?
}