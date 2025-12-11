package com.example.tryeexam.secondTrye.Presentation.Screens.MainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tryeexam.Data.repository.api.SupabaseAPI.BASE_URL
import com.example.tryeexam.Domain.model.Books
import com.example.tryeexam.Domain.model.User
import com.example.tryeexam.Domain.usecase.DeleteUseCase
import com.example.tryeexam.Domain.usecase.FetchUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.emptyList

class MainViewModel : ViewModel() {

    private val fetchUseCase = FetchUseCase()
    private val deleteUseCase = DeleteUseCase()

    private val books = MutableStateFlow<List<Books>>(emptyList())
    val dispayedBooks = MutableStateFlow<List<Books>>(emptyList())

    private val users = MutableStateFlow<List<Books>>(emptyList())
    val dispayedUsers = MutableStateFlow<List<Books>>(emptyList())

    var isLoading = MutableStateFlow(false)
    var error = MutableStateFlow("")

    fun loadData() {
        viewModelScope.launch {
            isLoading.value = true
            error.value = ""
            val result = fetchUseCase()
            books.value = result
            dispayedBooks.value = result

            isLoading.value = false
        }
    }

    fun deleteUser(userid: String){
        viewModelScope.launch {
            isLoading.value = true
            error.value = ""

            val result = deleteUseCase(userid)
            if(!result){
                error.value = "Не удалось удалить"
            }
            else{
                users.value = users.value.filter { it.id != userid }
                dispayedUsers.value = users.value
            }

            isLoading.value = false
        }
    }


}