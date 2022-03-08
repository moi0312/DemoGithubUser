package com.edlo.mydemoapp.repository.net.github

import com.edlo.mydemoapp.repository.net.github.ApiGitHub.PAGE_ITEMS
import com.edlo.mydemoapp.repository.net.github.ApiGitHub.SORT_ASC
import com.edlo.mydemoapp.repository.net.github.data.GithubUserData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object ApiGitHub {
    const val BASE_URL = "https://api.github.com/"

    const val GET_SEARCH_USERS = "search/users"
    const val GET_USERS = "users/{user}"

    const val PAGE_ITEMS = 30
    const val SORT_ASC = "asc"
    const val SORT_DESC = "desc"
}

interface ApiGitHubService {

    @GET(ApiGitHub.GET_SEARCH_USERS)
    suspend fun listUsers( @Query("q") q: String, @Query("page") page: Int = 1,
                   @Query("sort") sort: String = "id", @Query("order") order: String = SORT_ASC,
                   @Query("per_page") perPage: Int = PAGE_ITEMS
    ): GHResponse<List<GithubUserData>>

    @GET(ApiGitHub.GET_USERS)
    suspend fun getUserDatails(@Path("user") login: String): GHResponse<GithubUserData>
}