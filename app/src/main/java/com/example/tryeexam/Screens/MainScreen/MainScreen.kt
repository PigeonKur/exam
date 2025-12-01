package com.example.tryeexam.Screens.Main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tryeexam.Models.User

@Composable
fun MainScreen(navController: NavController) {
    val viewModel: MainViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Пользователи",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(onClick = { navController.navigate("addUser") }) {
                Text("Добавить")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Состояния
        val users by viewModel.users.collectAsState()
        val error by viewModel.error.collectAsState()

        if (error.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(error, color = Color.Red)
                Spacer(Modifier.height(8.dp))
                Button(onClick = { viewModel.loadUsers() }) {
                    Text("Повторить")
                }
            }
        } else if (users.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет пользователей")
            }
        } else {
            // Простой список
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(users) { user ->
                    UserCard(user = user, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun UserCard(user: User, viewModel: MainViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Логин: ${user.login}",
                color = Color.Gray
            )

            Spacer(Modifier.height(8.dp))

            // Простая кнопка с иконкой и текстом
            Button(
                onClick = { viewModel.deleteUser(user.id) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Удалить",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Удалить")
            }
        }
    }
}