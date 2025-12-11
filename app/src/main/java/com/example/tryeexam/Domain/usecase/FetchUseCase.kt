package com.example.tryeexam.Domain.usecase

import com.example.tryeexam.Data.repository.SupabaseRepositoryImpl
import com.example.tryeexam.Domain.model.Books
import com.example.tryeexam.Domain.model.User

class FetchUseCase {
    private val repository = SupabaseRepositoryImpl()

    suspend operator fun invoke() : List<Books>{
        return repository.fetchMethod()
    }
}