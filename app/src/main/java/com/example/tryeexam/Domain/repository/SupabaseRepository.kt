package com.example.tryeexam.Domain.repository

import com.example.tryeexam.Domain.model.Books
import com.example.tryeexam.Domain.model.User

interface SupabaseRepository{

    suspend fun LoginFunction(login: String, password: String) : User?
    suspend fun deleteMethod(userid: String) : Boolean
    suspend fun fetchMethod(): List<Books>
    suspend fun registerFunction(name : String, login: String, password: String) : Boolean
}