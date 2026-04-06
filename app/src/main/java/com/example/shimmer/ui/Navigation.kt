package com.example.shimmer.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.shimmer.ui.screens.*
import com.example.shimmer.ui.theme.PurpleAccent
import com.example.shimmer.ui.theme.DeepBlack
import com.example.shimmer.ui.theme.TextSecondary
import com.example.shimmer.ui.components.ShimmerText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBars = currentRoute != "onboarding" && currentRoute != "cleaner_onboarding"
    
    // Screens that are considered root level for the bottom navigation
    val rootScreens = listOf("customer_home", "available_jobs", "active_job", "cleaner_active_job", "profile")
    val isRootScreen = currentRoute?.split("/")?.get(0) in rootScreens

    Scaffold(
        topBar = {
            if (showBars && !isRootScreen && navController.previousBackStackEntry != null) {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        Text(
                            "BACK",
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable { navController.popBackStack() },
                            color = PurpleAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        },
        bottomBar = {
            if (showBars) {
                BottomNavigationBar(navController, currentRoute)
            }
        },
        containerColor = DeepBlack
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = "onboarding") {
                composable("onboarding") { OnboardingScreen(navController) }
                composable("customer_home") { CustomerHomeScreen(navController) }
                composable("job_request/{type}") { backStackEntry ->
                    val type = backStackEntry.arguments?.getString("type") ?: "CAR"
                    JobRequestScreen(navController, type)
                }
                composable("active_job") { ActiveJobScreen(navController) }
                composable("payment_confirmation") { PaymentConfirmationScreen(navController) }
                composable("rating_review") { RatingReviewScreen(navController) }
                
                // Cleaner side
                composable("cleaner_onboarding") { CleanerOnboardingScreen(navController) }
                composable("available_jobs") { AvailableJobsScreen(navController) }
                composable("cleaner_active_job") { CleanerActiveJobScreen(navController) }
                composable(
                    "job_details/{index}",
                    arguments = listOf(navArgument("index") { type = NavType.IntType })
                ) { backStackEntry ->
                    val index = backStackEntry.arguments?.getInt("index") ?: 0
                    JobDetailsScreen(navController, index)
                }

                // Placeholder Profile
                composable("profile") { ProfileScreen(navController) }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    Surface(
        color = Color(0xFF0A0A0A),
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, Color(0xFF1A1A1A))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isCleaner = UserSession.isCleanerMode
            
            NavItem("HOME", currentRoute?.contains("home") == true || currentRoute == "available_jobs") {
                val dest = if (isCleaner) "available_jobs" else "customer_home"
                navController.navigate(dest) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
            NavItem("ACTIVITY", currentRoute == "active_job" || currentRoute == "cleaner_active_job") {
                val dest = if (isCleaner) "cleaner_active_job" else "active_job"
                navController.navigate(dest) {
                    launchSingleTop = true
                }
            }
            NavItem("PROFILE", currentRoute == "profile") {
                navController.navigate("profile") {
                    launchSingleTop = true
                }
            }
        }
    }
}

@Composable
fun NavItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        ShimmerText(
            text = label,
            fontSize = 10,
            fontWeight = FontWeight.Black,
            color = if (isSelected) PurpleAccent else TextSecondary
        )
        if (isSelected) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(PurpleAccent, shape = RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        ShimmerText(text = "ACCOUNT", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
        ShimmerText(text = UserSession.userName, fontSize = 32, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(8.dp))
        ShimmerText(
            text = if (UserSession.isCleanerMode) "VERIFIED CLEANER" else "CUSTOMER",
            color = if (UserSession.isCleanerMode) PurpleAccent else TextSecondary,
            fontSize = 12,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        com.example.shimmer.ui.components.ShimmerCard {
            ShimmerText(text = "PAYMENT METHODS", fontWeight = FontWeight.Bold)
            ShimmerText(text = "VISA **** 9012", color = TextSecondary, fontSize = 12)
        }
        Spacer(modifier = Modifier.height(16.dp))
        com.example.shimmer.ui.components.ShimmerCard {
            ShimmerText(text = "NOTIFICATIONS", fontWeight = FontWeight.Bold)
            ShimmerText(text = "ON", color = PurpleAccent, fontSize = 12, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.weight(1f))
        com.example.shimmer.ui.components.ShimmerButton(
            text = "LOG OUT",
            onClick = { 
                UserSession.isCleanerMode = false
                navController.navigate("onboarding") {
                    popUpTo(0)
                }
            },
            containerColor = Color.Transparent,
            contentColor = Color.Red
        )
    }
}
