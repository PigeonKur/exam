package com.example.tryeexam.Screens.Register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryeexam.Models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

private const val apikey = "sb_publishable_7ZA2YpN1pJVk_SBtnCXU4w_bxH6AkPi"

class RegisterViewModel : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    private val supabaseURL = "https://speosihdvdoncaoabojt.supabase.co"

    fun register(name: String, login: String, password: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val success = registerMethod(name, login, password)
            if (success) {
                _registerState.value = RegisterState.Success
            } else {
                _registerState.value = RegisterState.Error("Ошибка регистрации")
            }
        }
    }

    suspend fun registerMethod(name: String, login: String, password: String): Boolean =
        try {
            withContext(Dispatchers.IO) {
                val url = URL("$supabaseURL/rest/v1/User")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("apikey", apikey)
                conn.setRequestProperty("Authorization", "Bearer $apikey")
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Prefer", "return=representation")
                conn.doOutput = true

                val userJson = JSONObject().apply {
                    put("id", UUID.randomUUID().toString())
                    put("Name", name)
                    put("Login", login)
                    put("Password", password)
                }

                conn.outputStream.use { output ->
                    output.write(userJson.toString().toByteArray())
                    output.flush()
                }

                val responseCode = conn.responseCode

                if (responseCode in 200..299) {
                    return@withContext true
                } else {
                    return@withContext false
                }
            }
        } catch (e: Exception) {
            false

        }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}