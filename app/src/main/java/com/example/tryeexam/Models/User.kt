package com.example.tryeexam.Models

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: String,
    val name: String,
    val login: String,
    val password: String
)