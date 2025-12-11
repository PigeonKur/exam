package com.example.tryeexam.Data.Model

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO (
    val id: String,
    val name: String,
    val login: String,
    val password: String
)