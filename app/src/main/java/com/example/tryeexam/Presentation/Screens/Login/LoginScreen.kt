package com.example.tryeexam.secondTrye.Presentation.Screens.Login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
){
    var login by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("") }

    var showError by remember{mutableStateOf(false)}
    var errorMessage by remember{mutableStateOf("")}

    val loginstate by loginViewModel.loginState.collectAsState()
    val currentUser by loginViewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextField(
            value = login,
            onValueChange = {
                login = it
                showError = false
            },
            label = { Text("Login") },
            isError = showError,
            modifier = Modifier.fillMaxWidth()


        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
                showError = false
            },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") },
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

        when(loginstate){
            is LoginState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.size(30.dp))
            }
            is LoginState.Error ->{
                errorMessage = (loginstate as LoginState.Error).message
                showError = true

                Button(
                    onClick = {
                        loginViewModel.login(login, password)
                    },
                    enabled = login.isNotEmpty() && password.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Войти")
                }
            }
            else -> {
                Button(
                    onClick = {
                        loginViewModel.login(login, password)
                    },
                    enabled = login.isNotEmpty() && password.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Войти")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Нет аккаунта? Зарегистрируйтесь")
        }
    }

    LaunchedEffect(key1 = currentUser) {
        if(currentUser != null){
            navController.navigate("main"){
                popUpTo("login")
            }
        }
    }

    LaunchedEffect(Unit) {
        loginViewModel.resetState()
    }

}