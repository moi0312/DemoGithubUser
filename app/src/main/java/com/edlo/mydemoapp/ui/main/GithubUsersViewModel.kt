package com.edlo.mydemoapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.edlo.mydemoapp.helper.SharedPreferencesHelper
import com.edlo.mydemoapp.repository.local.GithubUserDB
import com.edlo.mydemoapp.repository.net.ApiResult
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

    fun listGitHubUsers(key: String) {
        sharedPreferencesHelper.searchKey = key
        searchKey.value = key
        if( key.isNotEmpty() ) {
            viewModelScope.launch {
                val result = apiGitHubHelper.listUsers(key)
                when (result) {
                    is ApiResult.NetworkError -> Log.e(msg = "listData fail: NetworkError" )
                    is ApiResult.GenericError -> Log.e(msg = "listData fail: GenericError -> code${result.code} error: ${result.error}" )
                    is ApiResult.Success -> {
                        result.value?.items?.let { users ->
                            var dao = githubUserDB.githubUserDao()
                            dao.insertAll(users)

                            val searchResult = dao.findByLogin("%${searchKey.value}%") as ArrayList<GithubUserData>
                            githubUsers.postValue(searchResult)
                        }
                    }
                }
            }
        }
    }

    fun getUserDatails() {
        _currentSelectedUser.value?.let { user ->
            viewModelScope.launch {
                val result = apiGitHubHelper.getUserDatails(user.login)
                when (result) {
                    is ApiResult.NetworkError -> Log.e(msg = "listData fail: NetworkError" )
                    is ApiResult.GenericError -> Log.e(msg = "listData fail: GenericError -> code${result.code} error: ${result.error}" )
                    is ApiResult.Success -> {
                        result.value?.let { user ->
                            _currentSelectedUser.value = user
                            onUserDetailUpdate.onNext(user)
                        }
                    }
                }
            }
        }
    }

}
