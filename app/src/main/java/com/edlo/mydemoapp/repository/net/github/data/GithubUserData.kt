package com.edlo.mydemoapp.repository.net.github.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Fts4 //support full-text search(FTS)
@Entity(tableName = "table_github_user")
data class GithubUserData(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    @SerializedName("id") val id: Int,

    @ColumnInfo
    @SerializedName("login") val login: String,

    @ColumnInfo
    @SerializedName("avatar_url") val avatarUrl: String,

    @ColumnInfo(name = "html_url")
    @SerializedName("html_url") val htmlUrl: String,

    @ColumnInfo(name = "public_repos")
    @SerializedName("public_repos") val publicRepos: Int = 0,

    @ColumnInfo(name = "public_gists")
    @SerializedName("public_gists") val publicGists: Int = 0,

    @ColumnInfo
    @SerializedName("followers") val followers: Int = 0,

    @ColumnInfo(name = "following")
    @SerializedName("following") val following: Int = 0,

    @SerializedName("node_id") val nodeId: String,
    @SerializedName("gravatar_id") val gravatarId: String,
    @SerializedName("url") val url: String,
    @SerializedName("followers_url") val  followersUrl: String,
    @SerializedName("following_url") val followingUrl: String,
    @SerializedName("gists_url") val gistsUrl: String,
    @SerializedName("starred_url") val starredUrl: String,
    @SerializedName("subscriptions_url") val subscriptionsUrl: String,
    @SerializedName("organizations_url") val organizationsUrl: String,
    @SerializedName("repos_url") val reposUrl: String,
    @SerializedName("events_url") val eventsUrl: String,
    @SerializedName("received_events_url") val receivedEvents_url: String,
    @SerializedName("type") val type: String,
    @SerializedName("site_admin") val siteAdmin: Boolean,
    @SerializedName("score") val score: Int,
//    @SerializedName("name") val name: String,
//    @SerializedName("company") val company: String?,
//    @SerializedName("blog") val blog: String?,
//    @SerializedName("location") val location: String?,
//    @SerializedName("email") val email: String?,
//    @SerializedName("hireable") val hireable: String?,
//    @SerializedName("bio") val String?,
//    @SerializedName("twitter_username") val twitterUsername: String?,
//    @SerializedName("created_at") val createdAt: String, //"2022-01-27T14:23:41Z"
//    @SerializedName("updated_at") val updatedAt: String, //"2022-01-27T14:23:41Z"
)