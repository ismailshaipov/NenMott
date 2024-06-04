package com.example.nenmott.screens.main

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.nenmott.screens.main.bottomnav.BottomNavigationItemBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController
){
    Scaffold(
        bottomBar = {
            BottomNavigationItemBar(navController)
        }
    ){

    }
}