package com.example.tryeexam.Screens.AddUser

import androidx.compose.runtime.MutableState
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryeexam.Screens.Register.RegisterState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import kotlin.random.Random

private const val apikey = "sb_publishable_7ZA2YpN1pJVk_SBtnCXU4w_bxH6AkPi"

class AddViewModel  : ViewModel() {

    private val _addState = MutableStateFlow<AddState>(AddState.Idle)

    val addState: StateFlow<AddState> = _addState

    private val supabaseURL = "https://speosihdvdoncaoabojt.supabase.co"

    fun add(name: String, login: String){
        viewModelScope.launch {
            _addState.value = AddState.Loading
            val success = AddMethod(name, login)
            if(success){
                _addState.value = AddState.Success
            }
            else _addState.value = AddState.Error("Ошибка")
        }
    }

    suspend fun AddMethod(name: String, login: String): Boolean{
        try{
            withContext(Dispatchers.IO){
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
                    put("Password", Random.nextInt(100 ,5000))
                }

                conn.outputStream.use {    output ->
                    output.write(userJson.toString().toByteArray())
                    output.flush()
                }

                val responseCode = conn.responseCode
                    if(responseCode in 200..299) return@withContext true
                    else return@withContext false


                }

            } catch (e: Exception){
                false
        }
        return false
    }
    fun resetState() {
        _addState.value = AddState.Idle
    }
}


sealed class AddState {
    object Idle : AddState()
    object Loading : AddState()
    object Success : AddState()
    data class Error(val message: String) : AddState()
}