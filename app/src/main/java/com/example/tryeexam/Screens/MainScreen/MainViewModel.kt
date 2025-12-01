package com.example.tryeexam.Screens.Main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryeexam.Models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

private const val apikey = "sb_publishable_7ZA2YpN1pJVk_SBtnCXU4w_bxH6AkPi"

class MainViewModel : ViewModel() {

    private val supabaseURL = "https://speosihdvdoncaoabojt.supabase.co"

    // Просто список пользователей
    val users = MutableStateFlow<List<User>>(emptyList())
    val isLoading = MutableStateFlow(false)
    val error = MutableStateFlow("")

    fun loadUsers() {
        viewModelScope.launch {
            isLoading.value = true
            error.value = ""

            val result = fetchUsers()
            if (result.isNotEmpty()) {
                users.value = result
            } else {
                error.value = "Не удалось загрузить"
            }

            isLoading.value = false
        }
    }

    private suspend fun fetchUsers(): List<User> =
        withContext(Dispatchers.IO) {
            try {
                val url = URL("$supabaseURL/rest/v1/User?select=*")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.setRequestProperty("apikey", apikey)
                conn.setRequestProperty("Authorization", "Bearer $apikey")

                if (conn.responseCode !in 200..299) return@withContext emptyList()

                val response = conn.inputStream.bufferedReader().readText()
                val jsonArray = JSONArray(response)
                val result = mutableListOf<User>()

                for (i in 0 until jsonArray.length()) {
                    val json = jsonArray.getJSONObject(i)
                    result.add(User(
                        id = json.getString("id"),
                        name = json.getString("Name"),
                        login = json.getString("Login"),
                        password = json.getString("Password")
                    ))
                }

                return@withContext result
            } catch (e: Exception) {
                Log.e("UserViewModel", "Ошибка: ${e.message}")
                emptyList()
            }
        }


    fun deleteUser(userId: String){
        viewModelScope.launch {

            withContext(Dispatchers.IO) {
                val url = URL("$supabaseURL/rest/v1/User?id=eq.$userId")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "DELETE"
                conn.setRequestProperty("apikey", apikey)
                conn.setRequestProperty("Authorization", "Bearer $apikey")

                val responseCode = conn.responseCode
                responseCode in 200..299


            }


            loadUsers()
        }

    }
}