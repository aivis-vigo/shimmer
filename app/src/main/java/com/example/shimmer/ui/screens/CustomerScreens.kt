package com.example.shimmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun CustomerHomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        ShimmerText(text = "WELCOME", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
        ShimmerText(text = "SHIMMER", fontSize = 40, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(48.dp))
        
        ShimmerCard(onClick = { navController.navigate("job_request/CAR") }) {
            ShimmerText(text = "CAR CLEANING", fontSize = 20, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerText(text = "DEEP WASH + DETAILING", color = TextSecondary, fontSize = 12, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        ShimmerCard(onClick = { navController.navigate("job_request/HOUSE") }) {
            ShimmerText(text = "HOUSE CLEANING", fontSize = 20, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerText(text = "FULL HOME SANITIZATION", color = TextSecondary, fontSize = 12, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun JobRequestScreen(navController: NavController, type: String) {
    var location by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        ShimmerText(text = "REQUEST", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
        ShimmerText(text = "$type CLEANING", fontSize = 32, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("LOCATION") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurpleAccent,
                unfocusedBorderColor = Color.DarkGray,
                focusedLabelColor = PurpleAccent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("PREFERRED TIME") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurpleAccent,
                unfocusedBorderColor = Color.DarkGray,
                focusedLabelColor = PurpleAccent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = details,
            onValueChange = { details = it },
            label = { Text("DETAILS") },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurpleAccent,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        
        Spacer(modifier = Modifier.weight(1f))
        ShimmerButton(text = "GET OFFERS", onClick = { 
            // Create a real job in the cleaner's list
            val newJob = JobRequest(location.uppercase(), details.uppercase(), "150 AED")
            MockJobData.availableJobs.add(newJob)
            MockJobData.currentJob = newJob
            navController.navigate("active_job") 
        })
    }
}

@Composable
fun OffersList(navController: NavController, job: JobRequest) {
    val offers = job.offers

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        ShimmerText(text = "NEARBY", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
        ShimmerText(text = "AVAILABLE OFFERS", fontSize = 28, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(24.dp))
        
        if (offers.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = PurpleAccent)
                    Spacer(modifier = Modifier.height(16.dp))
                    ShimmerText(text = "WAITING FOR CLEANERS...", color = TextSecondary, fontSize = 14, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    ShimmerText(text = "LOG OUT AND SIGN IN AS CLEANER TO BID", color = TextSecondary, fontSize = 10)
                }
            }
        } else {
            offers.forEach { offer ->
                ShimmerCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            ShimmerText(text = offer.cleanerName, fontWeight = FontWeight.Bold)
                            ShimmerText(text = offer.rating, color = PurpleAccent, fontSize = 10, fontWeight = FontWeight.Bold)
                            ShimmerText(text = offer.eta, color = TextSecondary, fontSize = 10, fontWeight = FontWeight.Bold)
                        }
                        ShimmerText(text = offer.price, fontSize = 20, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                MockJobData.activeOffer = offer
                                MockJobData.isJobInProgress = true
                                MockJobData.resetTasks()
                                MockJobData.availableJobs.remove(job)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("ACCEPT", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Button(
                            onClick = { 
                                job.offers.remove(offer)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.DarkGray),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("DECLINE", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ActiveJobScreen(navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            if (MockJobData.isWaitingForRating) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(48.dp))
                    ShimmerText(text = "SERVICE COMPLETE", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
                    ShimmerText(text = "RATE YOUR EXPERIENCE", fontSize = 24, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(32.dp))
                    ShimmerButton(text = "GIVE FEEDBACK", onClick = { navController.navigate("rating_review") })
                }
            } else if (MockJobData.isJobInProgress) {
                ActiveTrackingView(navController)
            } else if (MockJobData.currentJob != null) {
                OffersList(navController, MockJobData.currentJob!!)
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(48.dp))
                    ShimmerText(text = "STATUS", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
                    ShimmerText(text = "NO ACTIVE SERVICE", fontSize = 28, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(16.dp))
                    ShimmerButton(text = "BOOK A CLEANER", onClick = { navController.navigate("customer_home") })
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(24.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                ShimmerText(text = "HISTORY", fontSize = 12, fontWeight = FontWeight.Bold, color = PurpleAccent)
                ShimmerText(text = "PREVIOUS SERVICES", fontSize = 20, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (MockJobData.finishedJobs.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    ShimmerText(text = "NO PREVIOUS SERVICES", color = TextSecondary, fontSize = 14, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            items(MockJobData.finishedJobs) { job ->
                Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                    ShimmerCard {
                        // Top row: location + amount
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                ShimmerText(text = job.location, fontWeight = FontWeight.Bold, color = PurpleAccent, fontSize = 14)
                                ShimmerText(text = job.details, color = TextSecondary, fontSize = 11, fontWeight = FontWeight.Bold)
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF1A1A1A), shape = RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                ShimmerText(text = job.amount, fontWeight = FontWeight.Black, color = Color.White, fontSize = 16)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        // Middle row: cleaner + date
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ShimmerText(text = "BY ${job.cleanerName.uppercase()}", color = Color.White, fontSize = 10, fontWeight = FontWeight.Bold)
                            ShimmerText(text = job.date, color = TextSecondary, fontSize = 10, fontWeight = FontWeight.Bold)
                        }
                        // Star rating
                        if (job.rating > 0) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) { i ->
                                    ShimmerText(
                                        text = "★",
                                        color = if (i < job.rating) PurpleAccent else Color(0xFF333333),
                                        fontSize = 14,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        // Feedback snippet
                        if (job.feedback.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            ShimmerText(
                                text = "\"${job.feedback}\"",
                                color = TextSecondary,
                                fontSize = 11
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // Completed badge
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF0D0D0D), shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            ShimmerText(text = "COMPLETED", color = PurpleAccent, fontSize = 9, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ActiveTrackingView(navController: NavController) {
    val cleanerName = MockJobData.activeOffer?.cleanerName ?: "YOUR CLEANER"
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFF050505)),
            contentAlignment = Alignment.Center
        ) {
            ShimmerText(text = "LIVE TRACKING: $cleanerName", fontSize = 12, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        }
        
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            ShimmerText(text = "STATUS", fontSize = 12, fontWeight = FontWeight.Bold, color = PurpleAccent)
            ShimmerText(text = "JOB IN PROGRESS", fontSize = 24, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { MockJobData.progress },
                modifier = Modifier.fillMaxWidth().height(2.dp),
                color = PurpleAccent,
                trackColor = Color(0xFF1A1A1A),
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            MockJobData.tasks.forEach { task ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(if (task.second) PurpleAccent else Color.DarkGray, shape = RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    ShimmerText(
                        text = task.first,
                        color = if (task.second) Color.White else TextSecondary,
                        fontSize = 12,
                        fontWeight = if (task.second) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            if (MockJobData.progress == 1f) {
                ShimmerButton(text = "PROCEED TO PAYMENT", onClick = { navController.navigate("payment_confirmation") })
            } else {
                ShimmerText(text = "$cleanerName IS WORKING", color = Color.White, fontSize = 12, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PaymentConfirmationScreen(navController: NavController) {
    val amount = MockJobData.activeOffer?.price ?: "120 AED"
    
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ShimmerText(text = "SUCCESS", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
        ShimmerText(text = "PAYMENT CONFIRMED", fontSize = 28, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(16.dp))
        ShimmerText(text = "$amount CHARGED TO CARD", color = TextSecondary, fontSize = 12, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(48.dp))
        ShimmerButton(text = "RATE YOUR CLEANER", onClick = { navController.navigate("rating_review") })
    }
}

@Composable
fun RatingReviewScreen(navController: NavController) {
    var rating by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        ShimmerText(text = "EXPERIENCE", fontSize = 14, fontWeight = FontWeight.Bold, color = PurpleAccent)
        ShimmerText(text = "HOW WAS THE JOB?", fontSize = 28, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(5) { i ->
                val currentRating = i + 1
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (currentRating <= rating) PurpleAccent else Color(0xFF1A1A1A), 
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { rating = currentRating }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = feedback,
            onValueChange = { feedback = it },
            label = { Text("FEEDBACK") },
            modifier = Modifier.fillMaxWidth().height(150.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurpleAccent,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        ShimmerButton(text = "SUBMIT", onClick = { 
            // Save feedback to the latest finished job
            if (MockJobData.finishedJobs.isNotEmpty()) {
                val latestJob = MockJobData.finishedJobs[0]
                latestJob.rating = rating
                latestJob.feedback = feedback
            }
            MockJobData.isJobInProgress = false
            MockJobData.isWaitingForRating = false
            MockJobData.currentJob = null
            navController.navigate("customer_home") {
                popUpTo("customer_home") { inclusive = true }
            }
        })
    }
}
