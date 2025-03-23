package com.example.studyflow.screen.onBoarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnBoardingScreen(
    onGetStarted: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Selamat Datang di StudyFlow!")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onGetStarted) {
            Text(text = "Get Started")
        }
    }
}