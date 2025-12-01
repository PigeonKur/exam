package com.example.tryeexam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tryeexam.Screens.Login.LoginViewModel
import com.example.tryeexam.Screens.Navigation.AppNavigator
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