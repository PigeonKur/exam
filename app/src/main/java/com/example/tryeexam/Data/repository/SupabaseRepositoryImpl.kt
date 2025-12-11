package com.example.tryeexam.Data.repository

import BooksDTO
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.tryeexam.Data.Model.UserDTO
import com.example.tryeexam.Data.repository.api.SupabaseAPI.BASE_URL
import com.example.tryeexam.Data.repository.mappers.toDTO
import com.example.tryeexam.Data.repository.mappers.toDomain
import com.example.tryeexam.Domain.model.Books
import com.example.tryeexam.Domain.model.User
import com.example.tryeexam.Domain.repository.SupabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.UUID

class SupabaseRepositoryImpl : SupabaseRepository {

    private val baseurl = BASE_URL
//    private val apiKey = apikey


    override suspend fun registerFunction(name: String, login: String, password: String): Boolean {
        try{
            withContext(Dispatchers.IO){
                var url = URL("$baseurl/collections/users/records")
                var conn = url.openConnection() as HttpURLConnection

                val domainUser = User(
                    id = "",
                    name = name,
                    login = login,
                    password = password
                )

                val userDTO = domainUser.toDTO()

                val userJson = """
                {
                    "name": "${userDTO.name}",
                    "login": "${userDTO.login}",
                    "password": "${userDTO.password}"
                }
            """.trimIndent()

                conn.requestMethod = "POST"
//                conn.setRequestProperty("apikey", apiKey)
//                conn.setRequestProperty("Authorization", "Bearer $apiKey")
                conn.setRequestProperty("Content-Type", "application/json")
//                conn.setRequestProperty("Prefer", "return=representation")
                conn.doOutput = true

                conn.outputStream.use {
                    it.write(userJson.toByteArray(Charsets.UTF_8))
                }

                val responsecode = conn.responseCode

                if (responsecode in 200 .. 299){
                    return@withContext true
                }
                else return@withContext false
            }
        }
        catch (e: Exception){
            false
        }
        return true
    }

    override suspend fun LoginFunction(login: String, password: String) : User?{
        return try{
            withContext(Dispatchers.IO){
                val filter = URLEncoder.encode("""login="$login" && password="$password"""", "UTF-8")
                val url = URL("$baseurl/collections/users/records?filter=$filter")

                val conn = url.openConnection() as HttpURLConnection

               conn.requestMethod = "GET"
//                conn.setRequestProperty("apikey", apiKey)
//                conn.setRequestProperty("Authorization", "Bearer $apiKey")

                val responsecode = conn.responseCode

                if(responsecode !in 200 .. 299){
                    Log.e("PB_LOGIN", "Bad response: ${conn.responseCode}")
                    return@withContext null
                }

                val response = conn.inputStream.bufferedReader().readText()

                val json = JSONObject(response)

                val items = json.getJSONArray("items")
                if(items.length() == 0){
                    return@withContext null
                }

                val obj = items.getJSONObject(0)

                val userDTO  = UserDTO(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    login = obj.getString("login"),
                    password = obj.getString("password")
                )

                val user = userDTO.toDomain()

                return@withContext user

            }
        }catch (e: Exception){
            null
        }
    }

   override suspend fun deleteMethod(userid: String) : Boolean{
        return try {
            withContext(Dispatchers.IO) {
                var url = URL("$baseurl/collections/users/records/$userid")
                var conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "DELETE"
//                conn.setRequestProperty("apikey", apiKey)
//                conn.setRequestProperty("Authorization", "Bearer $apiKey")

                val responseCode = conn.responseCode
                return@withContext responseCode in 200 .. 299
            }
        } catch (e: Exception){
            Log.e("Repository", "Ошибка удаления: ${e.message}")
            false
        }
    }

//    override suspend fun fetchMethod(): List<User> {
//        return withContext(Dispatchers.IO) {
//            try {
//                var url = URL("$baseurl/collections/users/records")
//                var conn = url.openConnection() as HttpURLConnection
//                conn.requestMethod = "GET"
////                conn.setRequestProperty("apikey", apiKey)
////                conn.setRequestProperty("Authorization", "Bearer $apiKey")
//
//
//                if (conn.responseCode !in 200..299) {
//                    return@withContext emptyList()
//                }
//
//                val response = conn.inputStream.bufferedReader().readText()
//                val json= JSONObject(response)
//
//                val items = json.getJSONArray("items")
//
//                val result = mutableListOf<User>()
//
//                for (i in 0 until items.length()) {
//                    val json = items.getJSONObject(i)
//                    result.add(
//                        User(
//                            id = json.getString("id"),
//                            name = json.getString("name"),
//                            password = json.getString("password"),
//                            login = json.getString("login")
//                        )
//                    )
//                }
//
//                return@withContext result
//            } catch (e: Exception) {
//                emptyList()
//            }
//        }
//    }

    override suspend fun fetchMethod(): List<Books> {
        return withContext(Dispatchers.IO) {
            try {
                var url = URL("$baseurl/collections/books/records")
                var conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
//                conn.setRequestProperty("apikey", apiKey)
//                conn.setRequestProperty("Authorization", "Bearer $apiKey")


                if (conn.responseCode !in 200..299) {
                    return@withContext emptyList()
                }

                val response = conn.inputStream.bufferedReader().readText()
                val json= JSONObject(response)

                val items = json.getJSONArray("items")

                val result = mutableListOf<Books>()

                for (i in 0 until items.length()) {
                    val json = items.getJSONObject(i)
                    val BooksDTO = BooksDTO(
                            id = json.getString("id"),
                            title = json.getString("title"),
                            author = json.getString("author"),
                            price = json.getString("price")
                    )
                    val book = BooksDTO.toDomain()
                    result.add(book)

                }

                return@withContext result
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

}