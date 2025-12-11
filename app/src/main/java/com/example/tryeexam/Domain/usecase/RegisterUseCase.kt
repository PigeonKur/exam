package com.example.tryeexam.Domain.usecase

import com.example.tryeexam.Data.repository.SupabaseRepositoryImpl

class RegisterUseCase {
    private val repository = SupabaseRepositoryImpl()

    suspend operator fun invoke(name: String, login: String, password: String): Boolean{
        return repository.registerFunction(name,login,password)
    }
}