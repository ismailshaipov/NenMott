package com.example.nenmott.screens.main.bottomnav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nenmott.screens.main.HomeScreen
import com.example.nenmott.screens.main.abc.ABC
import com.example.nenmott.screens.main.user.UserProfileScreen
import com.example.nenmott.sreens.login.LoginScreen

@Composable
fun Navigation(navController: NavController) {
    NavHost(navController = navController as NavHostController, startDestination = "UserProfile") {
        composable("ABC") {
            ABC()
        }
        composable("Home") {
            HomeScreen()
        }
        composable("UserProfile") {
            UserProfileScreen()
        }
    }
}
