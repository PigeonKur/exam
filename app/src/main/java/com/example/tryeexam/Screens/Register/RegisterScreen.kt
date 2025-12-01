package com.example.tryeexam.Screens.Register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    navController: NavController
) {
    val viewModel: RegisterViewModel = viewModel()

    var name by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val registerState by viewModel.registerState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                showError = false
            },
            label = { Text("Имя") },
            isError = showError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = login,
            onValueChange = {
                login = it
                showError = false
            },
            label = { Text("Логин") },
            isError = showError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                showError = false
            },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Пароль") },
            isError = showError,
            modifier = Modifier.fillMaxWidth()
        )

        if (showError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (registerState) {
            is RegisterState.Loading -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Регистрация...")
                }
            }

            is RegisterState.Error -> {
                errorMessage = (registerState as RegisterState.Error).message
                showError = true

                Button(
                    onClick = {
                        viewModel.register(name, login, password)
                    },
                    enabled = name.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Зарегистрироваться")
                }
            }

            is RegisterState.Success -> {
                Text(
                    "Регистрация успешна!",
                    color = Color.Green,
                    modifier = Modifier.padding(16.dp)
                )

                LaunchedEffect(Unit) {
                    delay(2000)
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            }

            else -> {
                Button(
                    onClick = {
                        viewModel.register(name, login, password)
                    },
                    enabled = name.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Зарегистрироваться")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.navigate("login") }
        ) {
            Text("Уже есть аккаунт? Войти")
        }
    }

    LaunchedEffect(key1 = registerState) {
        if (registerState is RegisterState.Error) {
            errorMessage = (registerState as RegisterState.Error).message
            showError = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }
}

//Политика для таблицы. На ALL, public
//alter policy "Policy with security definer functions"
//on "public"."User"
////to public
//using (
//true
//;