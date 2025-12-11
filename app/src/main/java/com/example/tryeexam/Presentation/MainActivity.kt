package com.example.tryeexam.Presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tryeexam.secondTrye.Presentation.Navigation.AppNavigator
import com.example.tryeexam.secondTrye.Presentation.Screens.Login.LoginViewModel
import com.example.tryeexam.ui.theme.TryeExamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TryeExamTheme {
                val loginViewModel : LoginViewModel = viewModel()
                MyTrye(loginViewModel = loginViewModel)
            }
        }
    }
}

@Composable
fun MyTrye(loginViewModel: LoginViewModel) {
    AppNavigator(loginViewModel = loginViewModel)
}