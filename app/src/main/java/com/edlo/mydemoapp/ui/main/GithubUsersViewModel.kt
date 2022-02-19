package com.edlo.mydemoapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.edlo.mydemoapp.helper.SharedPreferencesHelper
import com.edlo.mydemoapp.repository.local.GithubUserDB
import com.edlo.mydemoapp.repository.net.ApiResult
import com.edlo.mydemoapp.repository.net.github.ApiGitHub.PAGE_ITEMS
import com.edlo.mydemoapp.repository.net.github.ApiGitHubHelper
import com.edlo.mydemoapp.repository.net.github.data.GithubUserData
import com.edlo.mydemoapp.ui.base.BaseViewModel
import com.edlo.mydemoapp.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubUsersViewModel @Inject constructor() : BaseViewModel() {

    @Inject lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    @Inject lateinit var apiGitHubHelper: ApiGitHubHelper
    @Inject lateinit var githubUserDB: GithubUserDB

    private var currentPage: MutableLiveData<Int> = MutableLiveData(1)
    private var searchKey: MutableLiveData<String> = MutableLiveData("")

    fun getSearchKey(): LiveData<String> { return searchKey }
    private val _currentSelectedUser = MutableLiveData<GithubUserData>()

    val currentSelectedUser: LiveData<GithubUserData> = _currentSelectedUser
    fun setCurrentSelectedUser(data: GithubUserData?) {
        _currentSelectedUser.postValue(data)
    }

    val onSelectedUser: PublishSubject<GithubUserData> = PublishSubject.create()
    val onUserDetailUpdate: PublishSubject<GithubUserData> = PublishSubject.create()
    val onSearchUser: PublishSubject<String> = PublishSubject.create()

    private var githubUsers: MutableLiveData<ArrayList<GithubUserData>> = MutableLiveData(ArrayList())
    fun getGitHubUsers(): LiveData<ArrayList<GithubUserData>> { return githubUsers }

    private var noMoreData = false

    fun listGitHubUsers(key: String, page: Int = 1) {
        if( key.isNotEmpty() ) {
            if(sharedPreferencesHelper.searchKey != key) {
                sharedPreferencesHelper.searchKey = key
                searchKey.value = key
                currentPage.value = 1
                noMoreData = false
            } else {
                currentPage.value = page
            }
            onLoading.onNext(true)
            viewModelScope.launch {
                var dao = githubUserDB.githubUserDao()
                val result = apiGitHubHelper.listUsers(key, page = currentPage.value as Int)
                when (result) {
                    is ApiResult.Success -> {
                        result.value?.items?.let { users ->
                            noMoreData = users.size < PAGE_ITEMS
                            dao.insertAll(users)
                            postSearchResultsFromDB()
                        }
                    }
                    is ApiResult.NetworkError -> {
                        postSearchResultsFromDB()
                    }
                    is ApiResult.GenericError -> {
                        onLoading.onNext(false)
                        Log.e(msg = "listData fail: GenericError -> code${result.code} error: ${result.error}" )
                    }
                }
            }
        }
    }

    fun getMoreUsers() {
        if(!noMoreData) {
            listGitHubUsers(sharedPreferencesHelper.searchKey, currentPage.value!!+1)
        }
    }

    fun getUserDatails() {
        _currentSelectedUser.value?.let { user ->
            onLoading.onNext(true)
            viewModelScope.launch {
                var dao = githubUserDB.githubUserDao()
                val result = apiGitHubHelper.getUserDatails(user.login)
                when (result) {
                    is ApiResult.Success -> {
                        result.value?.let { user ->
                            dao.update(user)
                            _currentSelectedUser.value = user
                            onUserDetailUpdate.onNext(user)
                            postSearchResultsFromDB()
                        }
                    }
                    is ApiResult.NetworkError -> {
                        onLoading.onNext(false)
                        Log.e(msg = "listData fail: NetworkError" )
                    }
                    is ApiResult.GenericError -> {
                        onLoading.onNext(false)
                        Log.e(msg = "listData fail: GenericError -> code${result.code} error: ${result.error}" )
                    }
                }
            }
        }
    }

    private suspend fun postSearchResultsFromDB() {
        viewModelScope.launch {
            var dao = githubUserDB.githubUserDao()
            val searchResult = dao.findByLogin("%${searchKey.value}%", getCurrentLimitItems()) as ArrayList<GithubUserData>
            githubUsers.postValue(searchResult)
            onLoading.onNext(false)
        }
    }

    private fun getCurrentLimitItems() = PAGE_ITEMS.times(currentPage.value?:1)

}
