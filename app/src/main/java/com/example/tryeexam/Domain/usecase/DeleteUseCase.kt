package com.example.tryeexam.Domain.usecase

import com.example.tryeexam.Data.repository.SupabaseRepositoryImpl

class DeleteUseCase {
    private val repository = SupabaseRepositoryImpl()

    suspend operator fun invoke(userId: String) : Boolean{
        return repository.deleteMethod(userId)
    }
}