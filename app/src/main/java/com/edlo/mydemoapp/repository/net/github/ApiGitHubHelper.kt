package com.edlo.mydemoapp.repository.net.github

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.edlo.mydemoapp.BuildConfig
import com.edlo.mydemoapp.repository.net.ApiResult
import com.edlo.mydemoapp.repository.net.callApi
import com.edlo.mydemoapp.repository.net.github.data.GitHubBaseResponse
import com.edlo.mydemoapp.repository.net.github.data.GithubUserData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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

    private val dispatcher: CoroutineDispatcher by lazy { Dispatchers.IO }
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
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
        service = retrofit.create(ApiGitHubService::class.java)
    }

    suspend fun listUsers(q: String, sort: String? = null, order: String? = null,
              perPage: Int? = null, page: Int? = null): ApiResult<GitHubBaseResponse<List<GithubUserData>>?> {
        return callApi(dispatcher) {
            service.listUsers(q, sort, order, perPage, page)?.await()
        }
    }

    suspend fun getUserDatails(login: String): ApiResult<GithubUserData?> {
        return callApi(dispatcher) {
            service.getUserDatails(login)?.await()
        }
    }
}