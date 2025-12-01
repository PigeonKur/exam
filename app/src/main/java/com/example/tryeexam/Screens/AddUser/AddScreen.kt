package com.example.tryeexam.Screens.AddUser

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.example.tryeexam.Screens.Register.RegisterState
import kotlinx.coroutines.delay

@Composable
fun AddScreen(navController: NavController){

    val viewModel: AddViewModel = viewModel ()

    var name by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val addState by viewModel.addState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement =  Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Добавление",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it
                showError = false},
            label = { Text ("Имя")},
            isError = showError,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = login,
            onValueChange = {login = it
                showError = false},
            label = { Text ("Логин")},
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
        when(addState){
            is AddState.Loading ->{
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Добавление...")
                }
            }
            is AddState.Error -> {
                errorMessage = (addState as AddState.Error).message
                showError = true

                Button(
                    onClick = {
                        viewModel.add(name,login)
                    },
                    enabled = true,
                    modifier = Modifier.fillMaxWidth()
                ) {
                   Text("Добавить")
                }
            }
            is AddState.Success -> {
                Text(
                    "Добавление успешно",
                    modifier = Modifier.padding(16.dp)
                )

                LaunchedEffect(Unit) {
                    delay(2000)
                    navController.navigate("main")
                }
            }
            else -> {
                Button(
                    onClick = {
                        viewModel.add(name, login)
                    },
                    enabled = true,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Добавить")
                }
            }
        }
    }
    LaunchedEffect(key1 = addState) {
        if (addState is RegisterState.Error) {
            errorMessage = (addState as RegisterState.Error).message
            showError = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }
}