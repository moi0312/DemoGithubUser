package com.edlo.mydemoapp.repository.net.github.data

import com.google.gson.annotations.SerializedName

data class GitHubBaseResponse<T> (
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incomplete_results: Boolean,
    @SerializedName("items") val items: T,
) { }
