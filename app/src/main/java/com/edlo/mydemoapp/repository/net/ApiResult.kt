package com.edlo.mydemoapp.repository.net

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

sealed class ApiResult<out T> {
    data class Success<out T>(val value: T): ApiResult<T>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null): ApiResult<Nothing>()
    object NetworkError: ApiResult<Nothing>()
}

data class ErrorResponse(
    @SerializedName("detailMessage") var detailMessage: String
)

suspend fun <T> callApi(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ApiResult<T> {
    return withContext(dispatcher) {
        try {
            ApiResult.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ApiResult.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ApiResult.GenericError(code, errorResponse)
                }
//                is IllegalStateException -> {
//                    val code = throwable.message
//                    val errorResponse = convertErrorBody(throwable)
//                    ApiResult.GenericError(code, errorResponse)
//                }
                else -> {
                    ApiResult.GenericError(null, null)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    return try {
        throwable.response()?.errorBody()?.string()?.let {
            Gson().fromJson(it, ErrorResponse::class.java)
        }
    } catch (exception: Exception) {
        null
    }
}