package com.example.nenmott.screens.main

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.nenmott.screens.main.bottomnav.BottomNavigationItemBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
){
    Scaffold(
        bottomBar = {
            BottomNavigationItemBar()
        }
    ){

    }
}

@Composable
@Preview
fun MS(){
    MainScreen()
}