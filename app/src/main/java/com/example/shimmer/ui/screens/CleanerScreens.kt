package com.example.shimmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.shimmer.ui.components.ShimmerCard
import com.example.shimmer.ui.components.ShimmerText
import com.example.shimmer.ui.theme.PurpleAccent
import com.example.shimmer.ui.theme.TextSecondary

@Composable
fun CleanerOnboardingScreen(navController: NavController) {
    var step by remember { mutableIntStateOf(1) }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        ShimmerText(text = "PARTNER", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
        ShimmerText(text = "JOIN SHIMMER", fontSize = 32, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(8.dp))
        ShimmerText(text = "STEP $step OF 2", color = TextSecondary, fontSize = 12, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(48.dp))
        
        if (step == 1) {
            ShimmerCard(onClick = { step = 2 }) {
                ShimmerText(text = "INDIVIDUAL CLEANER", fontWeight = FontWeight.Bold)
                ShimmerText(text = "FREELANCE PROFESSIONAL", color = TextSecondary, fontSize = 12, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            ShimmerCard(onClick = { step = 2 }) {
                ShimmerText(text = "CLEANING COMPANY", fontWeight = FontWeight.Bold)
                ShimmerText(text = "TEAM AND FLEET MANAGEMENT", color = TextSecondary, fontSize = 12, fontWeight = FontWeight.Bold)
            }
        } else {
            ShimmerText(text = "VERIFICATION", fontWeight = FontWeight.Bold, fontSize = 14, color = PurpleAccent)
            Spacer(modifier = Modifier.height(16.dp))
            ShimmerCard {
                ShimmerText(text = "EMIRATES ID / TRADE LICENSE", color = TextSecondary, fontSize = 12, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                ShimmerText(text = "TAP TO UPLOAD DOCUMENT", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.weight(1f))
            ShimmerButton(text = "SUBMIT APPLICATION", onClick = { 
                UserSession.isCleanerMode = true
                navController.navigate("available_jobs") {
                    popUpTo("onboarding") { inclusive = true }
                }
            })
        }
    }
}

@Composable
fun AvailableJobsScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        ShimmerText(text = "BROWSE", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
        ShimmerText(text = "OPEN REQUESTS", fontSize = 28, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(24.dp))
        
        if (MockJobData.availableJobs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ShimmerText(text = "NO JOBS AVAILABLE", color = TextSecondary, fontSize = 14, fontWeight = FontWeight.Bold)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(MockJobData.availableJobs) { job ->
                    val hasPlacedOffer = job.offers.any { it.cleanerName == UserSession.cleanerName }
                    val isMyOfferAccepted = MockJobData.isJobInProgress && MockJobData.activeOffer?.cleanerName == UserSession.cleanerName && MockJobData.currentJob == job
                    
                    ShimmerCard {
                        ShimmerText(text = job.location, fontWeight = FontWeight.Bold, color = PurpleAccent)
                        ShimmerText(text = job.details, fontSize = 14)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ShimmerText(text = job.budgetRange, color = TextSecondary, fontWeight = FontWeight.Bold, fontSize = 14)
                            
                            if (isMyOfferAccepted) {
                                Button(
                                    onClick = { navController.navigate("cleaner_active_job") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("START JOB", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Black)
                                }
                            } else if (hasPlacedOffer) {
                                ShimmerText(text = "OFFER PENDING", color = PurpleAccent, fontSize = 12, fontWeight = FontWeight.Bold)
                            } else {
                                Button(
                                    onClick = { 
                                        job.offers.add(CleanerOffer(UserSession.cleanerName, "4.9 RATING", "15 MINS ETA", "120 AED"))
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text("OFFER", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CleanerActiveJobScreen(navController: NavController) {
    val isAssignedCleaner = MockJobData.activeOffer?.cleanerName == UserSession.cleanerName
    if (!MockJobData.isJobInProgress || MockJobData.currentJob == null || !isAssignedCleaner) {
        CleanerHistoryScreen(navController)
        return
    }

    val job = MockJobData.currentJob!!

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth().height(150.dp).background(Color(0xFF050505)),
            contentAlignment = Alignment.Center
        ) {
            ShimmerText(text = "MAP: ${job.location}", fontSize = 10, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        }
        
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth()
        ) {
            ShimmerText(text = "ACTIVE", fontSize = 12, fontWeight = FontWeight.Bold, color = PurpleAccent)
            ShimmerText(text = "JOB CHECKLIST", fontSize = 24, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(16.dp))
            
            LinearProgressIndicator(
                progress = { MockJobData.progress },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = PurpleAccent,
                trackColor = Color(0xFF1A1A1A),
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            MockJobData.tasks.forEachIndexed { index, task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable {
                            MockJobData.tasks[index] = task.first to !task.second
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                if (task.second) PurpleAccent else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .background(
                                if (!task.second) Color.DarkGray else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    ShimmerText(
                        text = task.first,
                        color = if (task.second) Color.White else TextSecondary,
                        fontWeight = if (task.second) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            if (MockJobData.progress == 1f) {
                ShimmerButton(
                    text = "FINISH JOB",
                    onClick = { 
                        MockJobData.finishedJobs.add(0, FinishedJob(
                            location = job.location, 
                            details = job.details, 
                            date = "TODAY", 
                            amount = MockJobData.activeOffer?.price ?: job.budgetRange,
                            cleanerName = MockJobData.activeOffer?.cleanerName ?: UserSession.cleanerName
                        ))
                        MockJobData.availableJobs.remove(job)
                        MockJobData.isJobInProgress = false
                        MockJobData.isWaitingForRating = true
                        MockJobData.currentJob = null
                        MockJobData.activeOffer = null
                        navController.navigate("available_jobs") 
                    }
                )
            } else {
                ShimmerText(
                    text = "COMPLETE ALL TASKS TO FINISH",
                    color = TextSecondary,
                    fontSize = 12,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun CleanerHistoryScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        ShimmerText(text = "STATUS", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
        ShimmerText(text = "NO ACTIVE JOB", fontSize = 28, fontWeight = FontWeight.Black)
        
        Spacer(modifier = Modifier.height(48.dp))
        
        ShimmerText(text = "HISTORY", fontSize = 12, fontWeight = FontWeight.Bold, color = PurpleAccent)
        ShimmerText(text = "COMPLETED JOBS", fontSize = 20, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(16.dp))
        
        if (MockJobData.finishedJobs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                ShimmerText(text = "NO COMPLETED JOBS YET", color = TextSecondary, fontSize = 14, fontWeight = FontWeight.Bold)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.weight(1f)) {
                itemsIndexed(MockJobData.finishedJobs) { index, job ->
                    ShimmerCard(onClick = { navController.navigate("job_details/$index") }) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                ShimmerText(text = job.location, fontWeight = FontWeight.Bold, color = PurpleAccent)
                                ShimmerText(text = job.details, fontSize = 12)
                                ShimmerText(text = job.date, color = TextSecondary, fontSize = 10, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                ShimmerText(text = job.amount, fontWeight = FontWeight.Black, color = Color.White)
                                if (job.rating > 0) {
                                    ShimmerText(text = "${job.rating} ★", color = PurpleAccent, fontSize = 12, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JobDetailsScreen(navController: NavController, jobIndex: Int) {
    val job = MockJobData.finishedJobs.getOrNull(jobIndex) ?: return

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        ShimmerText(text = "DETAILS", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
        ShimmerText(text = job.location, fontSize = 28, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(16.dp))
        
        ShimmerCard {
            ShimmerText(text = "SERVICE INFO", fontSize = 12, fontWeight = FontWeight.Bold, color = PurpleAccent)
            Spacer(modifier = Modifier.height(8.dp))
            ShimmerText(text = job.details, fontWeight = FontWeight.Bold)
            ShimmerText(text = job.date, color = TextSecondary, fontSize = 14)
            Spacer(modifier = Modifier.height(16.dp))
            ShimmerText(text = "EARNINGS", fontSize = 12, fontWeight = FontWeight.Bold, color = PurpleAccent)
            ShimmerText(text = job.amount, fontSize = 20, fontWeight = FontWeight.Black)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        ShimmerText(text = "CUSTOMER FEEDBACK", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
        Spacer(modifier = Modifier.height(8.dp))
        
        if (job.rating > 0) {
            ShimmerCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { i ->
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    if (i < job.rating) PurpleAccent else Color(0xFF1A1A1A), 
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
                if (job.feedback.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    ShimmerText(text = job.feedback, color = Color.White)
                }
            }
        } else {
            ShimmerText(text = "Awaiting feedback...", color = TextSecondary, fontSize = 14)
        }
        
        Spacer(modifier = Modifier.weight(1f))
        ShimmerButton(text = "BACK TO HISTORY", onClick = { navController.popBackStack() })
    }
}
