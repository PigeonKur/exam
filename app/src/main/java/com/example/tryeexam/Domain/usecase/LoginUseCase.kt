package com.example.tryeexam.Domain.usecase

import com.example.tryeexam.Data.repository.SupabaseRepositoryImpl
import com.example.tryeexam.Domain.model.User

class LoginUseCase {
    private val repository = SupabaseRepositoryImpl()

    suspend operator fun invoke(login: String, password: String): User?{
        return repository.LoginFunction(login,password)
    }
}