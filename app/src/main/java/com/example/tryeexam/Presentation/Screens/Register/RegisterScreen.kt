package com.example.tryeexam.secondTrye.Presentation.Screens.Register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    navController: NavController
) {
    val viewModel: RegisterViewModel = viewModel ()

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }

    val registerState by viewModel.registerState.collectAsState()


    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Регистрация",
            modifier = Modifier.padding(25.dp)
        )

        TextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = login,
            onValueChange = {
                login = it
            },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(10.dp))

        when(registerState){
            is RegisterState.Loading ->{
                Column( horizontalAlignment = Alignment.CenterHorizontally){
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Регистрация...")
                }
            }
            is RegisterState.Error -> {
                Button(
                    onClick = {
                        viewModel.register(name,login,password)
                    },
                    enabled = name.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Зарегистрироваться")
                }
            }
            is RegisterState.Success -> {
                Text("Регистрация успешна", color = Color.Green, modifier = Modifier.padding(15.dp))

                LaunchedEffect(Unit) {
                    delay(2000)
                    navController.navigate("login"){
                        popUpTo("register")
                    }
                }
            }
            else ->
                Button(
                    onClick = {
                        viewModel.register(name,login,password)
                    },
                    enabled = name.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Зарегистрироваться")
                }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.navigate("login") }
        ) {
            Text("Уже есть аккаунт? Войти")
        }


        LaunchedEffect(Unit) {
            viewModel.resetState()
        }
    }

}