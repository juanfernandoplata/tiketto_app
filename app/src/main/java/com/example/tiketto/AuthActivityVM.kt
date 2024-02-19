package com.example.tiketto

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.POST
import retrofit2.http.Body

data class Credentials (
    val username: String,
    val password: String
)

data class AccessToken (
    val accessToken : String
)

interface Authentication {
    @POST("authenticate")
    suspend fun authenticate(
        @Body credentials : Credentials
    ) : AccessToken
}

object Authenticator {
    fun construct() : Authentication {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Authentication::class.java)
    }
}

class AuthActivityVM : ViewModel() {
    private val _authToken = MutableLiveData<String>()
    val authToken: LiveData<String> get() = _authToken

    fun authenticate(username : String, password : String) {
        val authenticator = Authenticator.construct()

        viewModelScope.launch {
            try {
                val accessToken = authenticator.authenticate(Credentials(username, password)).accessToken
                _authToken.postValue(accessToken)
            } catch (e: HttpException) {
                if(e.code() == 401){
                    _authToken.postValue("null")
                }
            }
        }
    }
}