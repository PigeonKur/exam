package com.example.tryeexam.secondTrye.Presentation.Screens.Register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryeexam.Data.repository.api.SupabaseAPI.BASE_URL
import com.example.tryeexam.Domain.usecase.RegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

class RegisterViewModel : ViewModel(){

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState : StateFlow<RegisterState> = _registerState

    private val registerUseCase = RegisterUseCase()

    fun register(name : String, login: String, password: String){
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            val success = registerUseCase(name,login,password)
            if(success){
                _registerState.value = RegisterState.Success
            }
            else _registerState.value = RegisterState.Error("Не удалось зарегестрироваться")
        }
    }
    fun resetState(){
        _registerState.value = RegisterState.Idle
    }

}

sealed class RegisterState {
    object Idle: RegisterState()
    object Loading: RegisterState()
    object  Success : RegisterState()
    data class Error(val message: String): RegisterState()

}