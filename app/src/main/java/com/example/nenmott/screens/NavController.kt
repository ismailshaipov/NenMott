package com.example.nenmott.screens

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nenmott.screens.login.RegScreen
import com.example.nenmott.screens.main.MainScreen
import com.example.nenmott.screens.login.LoginScreen
import com.example.nenmott.viewmodels.UserProfileViewModel

@Composable
fun NavigationHost(userViewModel: UserProfileViewModel = viewModel()) {
    val navController = rememberNavController()
    val isLoggedIn by userViewModel.isLoggedIn.collectAsState()

    NavHost(navController = navController, startDestination = if (isLoggedIn) "mainScreen" else "login") {
        composable(
                "login",
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700)) + fadeIn(animationSpec = tween(700))
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700)) + fadeOut(animationSpec = tween(700))
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700)) + fadeIn(animationSpec = tween(700))
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700)) + fadeOut(animationSpec = tween(700))
                }
            ) {
                LoginScreen(navController = navController, userViewModel)
        }
        composable(
                "register",
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700)) + fadeIn(animationSpec = tween(700))
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700)) + fadeOut(animationSpec = tween(700))
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700)) + fadeIn(animationSpec = tween(700))
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700)) + fadeOut(animationSpec = tween(700))
                }
        ) {
                RegScreen(navController = navController)
        }
        composable("mainScreen") {
                MainScreen(navController)
        }
    }
}
