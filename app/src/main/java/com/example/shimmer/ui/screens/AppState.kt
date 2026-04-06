package com.example.shimmer.ui.screens

import androidx.compose.runtime.*

// Shared data state for the marketplace
object MockJobData {
    var isJobInProgress by mutableStateOf(false)
    var isWaitingForRating by mutableStateOf(false)
    var currentJob by mutableStateOf<JobRequest?>(null)
    var activeOffer by mutableStateOf<CleanerOffer?>(null)
    
    val availableJobs = mutableStateListOf<JobRequest>()

    val tasks = mutableStateListOf<Pair<String, Boolean>>()
    
    val progress: Float
        get() = if (tasks.isEmpty()) 0f else tasks.count { it.second }.toFloat() / tasks.size
        
    val finishedJobs = mutableStateListOf<FinishedJob>(
        FinishedJob(
            location = "DUBAI MARINA",
            details = "CAR CLEANING",
            date = "24 MAR 2026",
            amount = "180 AED",
            cleanerName = "Ahmed K.",
            rating = 5,
            feedback = "Excellent job, car looks brand new!"
        ),
        FinishedJob(
            location = "JUMEIRAH BEACH RD",
            details = "HOUSE CLEANING",
            date = "18 MAR 2026",
            amount = "320 AED",
            cleanerName = "Sara M.",
            rating = 4,
            feedback = "Very thorough, happy with the result."
        ),
        FinishedJob(
            location = "DOWNTOWN DUBAI",
            details = "CAR CLEANING",
            date = "10 MAR 2026",
            amount = "150 AED",
            cleanerName = "Khalid R.",
            rating = 5,
            feedback = ""
        )
    )

    fun resetTasks() {
        tasks.clear()
        tasks.addAll(listOf(
            "Exterior Pressure Wash" to false,
            "Wheel & Rim Cleaning" to false,
            "Interior Vacuuming" to false,
            "Dashboard Polishing" to false,
            "Glass Cleaning" to false
        ))
    }
}

data class CleanerOffer(
    val cleanerName: String,
    val rating: String,
    val eta: String,
    val price: String
)

data class JobRequest(
    val location: String, 
    val details: String, 
    val budgetRange: String,
    val offers: MutableList<CleanerOffer> = mutableStateListOf()
)

data class FinishedJob(
    val location: String, 
    val details: String, 
    val date: String, 
    val amount: String,
    val cleanerName: String = "Ahmed K.",
    var rating: Int = 0,
    var feedback: String = ""
)

// Global role state for the prototype
object UserSession {
    var isCleanerMode by mutableStateOf(false)
    var userName by mutableStateOf("GUEST")
    var cleanerName: String 
        get() = userName
        set(value) { userName = value }
}
