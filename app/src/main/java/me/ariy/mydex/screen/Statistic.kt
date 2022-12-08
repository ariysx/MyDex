package me.ariy.mydex.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

@Composable
fun Statistic(
    navController: NavHostController
) {
    val context = LocalContext.current
    Text(text = "Statistics")
}