package com.example.nenmott.screens

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nenmott.sreens.login.LoginScreen
import com.example.nenmott.screens.login.RegScreen
import com.example.nenmott.screens.main.MainScreen

@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
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
                LoginScreen(navController = navController)
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
                MainScreen()
        }
    }
}
