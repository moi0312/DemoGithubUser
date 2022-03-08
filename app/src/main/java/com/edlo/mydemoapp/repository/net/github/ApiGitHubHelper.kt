package com.edlo.mydemoapp.repository.net.github

import com.edlo.mydemoapp.BuildConfig
import com.edlo.mydemoapp.repository.net.ApiCallAdapterFactory
import com.edlo.mydemoapp.repository.net.ApiResult
import com.edlo.mydemoapp.repository.net.github.data.GitHubBaseResponse
import com.edlo.mydemoapp.repository.net.github.data.GithubUserData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiGitHubHelper @Inject constructor() {

    private var okHttpClient: OkHttpClient
    private var retrofit: Retrofit

    private var service: ApiGitHubService

    init {
        val okHttpClientBuilder = OkHttpClient().newBuilder()
            .addInterceptor { chain ->
                var reqBuilder = chain.request().newBuilder()
                    .url(chain.request().url)
//                    .addHeader("accept", "application/vnd.github.v3+json")
                chain.proceed(reqBuilder.build())
            }
        if( BuildConfig.PRINT_LOG ) {
            okHttpClientBuilder.addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY))
        }
        okHttpClient = okHttpClientBuilder.build()

        retrofit = Retrofit.Builder()
            .baseUrl(ApiGitHub.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiCallAdapterFactory())
            .client(okHttpClient)
            .build()
        service = retrofit.create(ApiGitHubService::class.java)
    }

    suspend fun listUsers(q: String, page: Int = 1): ApiResult<GitHubBaseResponse<List<GithubUserData>>, Error> {
        return service.listUsers(q, page)
    }

    suspend fun getUserDatails(login: String): ApiResult<GithubUserData, Error> {
        return service.getUserDatails(login)
    }
}