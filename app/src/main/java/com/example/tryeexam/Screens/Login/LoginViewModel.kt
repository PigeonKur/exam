package com.example.tryeexam.Screens.Login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tryeexam.Models.User
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

private const val apikey = "sb_publishable_7ZA2YpN1pJVk_SBtnCXU4w_bxH6AkPi"

class LoginViewModel : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val supabaseURL = "https://speosihdvdoncaoabojt.supabase.co"

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = loginMethod(login, password)
            if (user != null) {
                _currentUser.value = user
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Неверный логин или пароль")
            }
        }
    }

    // Метод для сброса состояния
    fun resetState() {
        _authState.value = AuthState.Idle
    }

    suspend fun loginMethod(login: String, password: String): User? =
        try {
            withContext(Dispatchers.IO) {
                val url = URL("$supabaseURL/rest/v1/User?Login=eq.$login&Password=eq.$password&select=*")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.setRequestProperty("apikey", apikey)
                conn.setRequestProperty("Authorization", "Bearer $apikey") // Исправлено: Authorization

                val responseCode = conn.responseCode
                Log.d("LoginViewModel", "Response code: $responseCode")

                if (responseCode !in 200..299) {
                    Log.e("LoginViewModel", "HTTP error: $responseCode")
                    return@withContext null
                }

                val response = conn.inputStream.bufferedReader().readText()
                Log.d("LoginViewModel", "Response: $response")

                val jsonArray = JSONArray(response)
                if (jsonArray.length() == 0) {
                    Log.e("LoginViewModel", "Пользователь не найден или неверный пароль")
                    return@withContext null
                }

                val json = jsonArray.getJSONObject(0)
                val user = User(
                    id = json.getString("id"),
                    name = json.getString("Name"),
                    login = json.getString("Login"),
                    password = json.getString("Password")
                )

                Log.d("LoginViewModel", "User found: ${user.login}")
                return@withContext user
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Ошибка входа: ${e.message}", e)
            null
        }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}