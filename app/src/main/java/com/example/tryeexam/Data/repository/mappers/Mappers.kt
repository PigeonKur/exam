package com.example.tryeexam.Data.repository.mappers

import BooksDTO
import com.example.tryeexam.Data.Model.UserDTO
import com.example.tryeexam.Domain.model.Books
import com.example.tryeexam.Domain.model.User

fun User.toDTO() = UserDTO(
    id = id,
    name = name,
    login = login,
    password = password
)

fun Books.toDTO() = BooksDTO(
    id = id,
    title = title,
    author = author,
    price = price
)

fun UserDTO.toDomain() = User(
    id = id,
    name = name,
    login = login,
    password = password
)

fun BooksDTO.toDomain() = Books(
    id = id,
    title = title,
    author = author,
    price = price
)