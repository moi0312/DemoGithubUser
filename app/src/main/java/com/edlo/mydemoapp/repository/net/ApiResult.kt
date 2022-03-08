package com.edlo.mydemoapp.repository.net

import okhttp3.Request
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

sealed class ApiResult<out T: Any, out U : Any> {
    data class Success<T: Any>(val body: T): ApiResult<T, Nothing>()
    data class ApiError<U: Any>(val body: U, val code: Int): ApiResult<Nothing, U>()
    data class NetworkError(val error: IOException): ApiResult<Nothing, Nothing>()
    data class GenericError(val error: Throwable?): ApiResult<Nothing, Nothing>()
}

internal class ApiCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<ApiResult<S, E>> {

    override fun enqueue(callback: Callback<ApiResult<S, E>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()
                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(this@ApiCall, Response.success(ApiResult.Success(body)))
                    } else { // Response is successful but the body is null
                        callback.onResponse(this@ApiCall, Response.success(ApiResult.GenericError(null)))
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try { errorConverter.convert(error) }
                            catch (ex: Exception) { null }
                    }
                    if (errorBody != null) {
                        callback.onResponse(this@ApiCall, Response.success(ApiResult.ApiError(errorBody, code)))
                    } else {
                        callback.onResponse(this@ApiCall, Response.success(ApiResult.GenericError(null)))
                    }
                }
            }

            override fun onFailure(call: Call<S>, t: Throwable) {
                val apiResult = when (t) {
                    is IOException -> ApiResult.NetworkError(t)
                    else -> ApiResult.GenericError(t)
                }
                callback.onResponse(this@ApiCall, Response.success(apiResult))
            }
        })
    }

    override fun clone() = ApiCall(delegate.clone(), errorConverter)

    override fun execute(): Response<ApiResult<S, E>> {
        throw UnsupportedOperationException("ApiCall doesn't support execute")
    }

    override fun isExecuted() = delegate.isExecuted
    override fun cancel() = delegate.cancel()
    override fun isCanceled() = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout() = delegate.timeout()
}

class ApiCallAdapter<S : Any, E : Any>(
    private val successType: Type, private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<ApiResult<S, E>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<ApiResult<S, E>> {
        return ApiCall(call, errorBodyConverter)
    }
}

class ApiCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type,
                 annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) { return null }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<ApiResult<<Foo>> or Call<ApiResult<out Foo>>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)
        // if the response type is not ApiResponse then we can't handle this type, so we return null
        if (getRawType(responseType) != ApiResult::class.java) { return null }

        // the response type is ApiResponse and should be parameterized
        check(responseType is ParameterizedType) { "Response must be parameterized as ApiResult<Foo> or ApiResult<out Foo>" }

        val successBodyType = getParameterUpperBound(0, responseType)
        val errorBodyType = getParameterUpperBound(1, responseType)

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<Any>(null, errorBodyType, annotations)

        return ApiCallAdapter<Any, Any>(successBodyType, errorBodyConverter)
    }
}