package com.example.tryeexam.secondTrye.Presentation.Screens.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryeexam.Data.repository.api.SupabaseAPI.BASE_URL
import com.example.tryeexam.Domain.model.User
import com.example.tryeexam.Domain.usecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState : StateFlow<LoginState> = _loginState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser : StateFlow<User?> = _currentUser

    private val loginUseCase = LoginUseCase()

    fun login(login: String, password: String){
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val users = loginUseCase(login, password)
            if(users != null){
                _currentUser.value = users
                _loginState.value = LoginState.Succes
            }
            else _loginState.value = LoginState.Error("Не правильный логин либо пароль")

        }
    }
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState{
    object Idle: LoginState()
    object Loading: LoginState()
    data class Error(val message: String): LoginState()
    object Succes: LoginState()
}