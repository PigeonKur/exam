package com.example.tryeexam.secondTrye.Presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tryeexam.secondTrye.Presentation.Screens.Login.LoginScreen
import com.example.tryeexam.secondTrye.Presentation.Screens.Login.LoginViewModel
import com.example.tryeexam.secondTrye.Presentation.Screens.MainScreen.MainScreen
import com.example.tryeexam.secondTrye.Presentation.Screens.Register.RegisterScreen

@Composable
fun AppNavigator(loginViewModel: LoginViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                loginViewModel = loginViewModel
            )
        }
        composable("register") {
            RegisterScreen(
                navController = navController
            )
        }
        composable("main") {
            MainScreen(navController = navController)
        }

    }
}