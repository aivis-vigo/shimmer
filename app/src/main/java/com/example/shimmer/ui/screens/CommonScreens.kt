package com.example.shimmer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shimmer.ui.components.ShimmerButton
import com.example.shimmer.ui.components.ShimmerText
import com.example.shimmer.ui.theme.PurpleAccent
import com.example.shimmer.ui.theme.TextSecondary

@Composable
fun OnboardingScreen(navController: NavController) {
    var showLoginDialog by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("") } // "CLEANER" or "CUSTOMER"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ShimmerText(
            text = "SHIMMER",
            fontSize = 56,
            fontWeight = FontWeight.Black,
            color = PurpleAccent
        )
        Spacer(modifier = Modifier.height(4.dp))
        ShimmerText(
            text = "CLEANING ON DEMAND",
            fontSize = 12,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(64.dp))
        
        ShimmerButton(
            text = "I AM A CLEANER",
            onClick = { 
                selectedRole = "CLEANER"
                showLoginDialog = true 
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ShimmerButton(
            text = "I NEED A CLEANER",
            onClick = { 
                selectedRole = "CUSTOMER"
                showLoginDialog = true
            },
            containerColor = Color.Transparent,
            contentColor = PurpleAccent
        )
    }

    if (showLoginDialog) {
        AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            containerColor = Color(0xFF1A1A1A),
            title = {
                ShimmerText(text = "LOGIN AS $selectedRole", fontSize = 18, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    ShimmerText(text = "SELECT AN ACCOUNT TO CONTINUE", color = TextSecondary, fontSize = 12)
                    Spacer(modifier = Modifier.height(16.dp))
                    if (selectedRole == "CLEANER") {
                        LoginOption("AHMED K. (4.9 ★)") {
                            UserSession.userName = "AHMED K."
                            UserSession.isCleanerMode = true
                            showLoginDialog = false
                            navController.navigate("available_jobs") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LoginOption("SAJID M. (5.0 ★)") {
                            UserSession.userName = "SAJID M."
                            UserSession.isCleanerMode = true
                            showLoginDialog = false
                            navController.navigate("available_jobs") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    } else {
                        LoginOption("JOHN DOE") {
                            UserSession.userName = "JOHN DOE"
                            UserSession.isCleanerMode = false
                            showLoginDialog = false
                            navController.navigate("customer_home") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
fun LoginOption(name: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2A2A)),
        shape = RoundedCornerShape(12.dp)
    ) {
        ShimmerText(text = name, fontSize = 14, fontWeight = FontWeight.Bold)
    }
}
