# Shimmer — On-Demand Cleaning Marketplace

Shimmer is a functional mobile app prototype for a cleaning marketplace in the UAE, similar to Uber but for car and house cleaning. Customers can post cleaning requests, receive price bids from verified cleaners, select their preferred professional, and track the job's progress in real-time.

## Project Overview

This prototype demonstrates a dual-sided marketplace experience:
- **Customer Side:** Post requests, browse offers, track live cleaning progress, make mock payments, and provide feedback.
- **Cleaner Side:** Browse open requests, place price bids, manage active jobs via a checklist, and view job history with customer feedback.

### Key Features
- **Role-Based Login:** Mock login system to switch between specific Customer (John Doe) and Cleaner (Ahmed K., Sajid M.) profiles.
- **Dynamic Marketplace:** Jobs created by customers appear live in the cleaners' "Available Jobs" feed.
- **Interactive Bidding:** Cleaners place offers that customers must accept or decline before work begins.
- **Synchronized Progress:** A shared checklist allows cleaners to check off tasks (e.g., "Interior Vacuuming") which updates the customer's tracking view in real-time.
- **Feedback Loop:** Persistent rating and review system that links customer feedback directly to the cleaner's history.
- **Premium Design:** Dark luxury theme with deep black backgrounds, purple accents, and typography-driven hierarchy.

## Tech Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Navigation:** Navigation Compose
- **State Management:** Snapshot State (MutableStateLists and Objects) for real-time reactivity between roles.

## How to Run

1. **Clone/Open in Android Studio:** Open the project in the latest version of Android Studio (Ladybug or newer recommended).
2. **Gradle Sync:** Ensure the project syncs successfully with the provided `build.gradle.kts` and `libs.versions.toml`.
3. **Run on Emulator/Device:** Target an API level 35+ device for the best experience.
4. **Test the Marketplace Flow:**
   - **Step 1:** Log in as **"JOHN DOE"** (Customer).
   - **Step 2:** Create a cleaning request from the Home screen.
   - **Step 3:** Navigate to the **Activity** tab (you'll see "Waiting for Cleaners").
   - **Step 4:** Go to **Profile** -> **Log Out**.
   - **Step 5:** Log in as **"AHMED K."** (Cleaner).
   - **Step 6:** Go to the Home tab and click **OFFER** on the request you just created.
   - **Step 7:** Log out and log back in as **"JOHN DOE"**.
   - **Step 8:** Go to the **Activity** tab, see the offer from Ahmed K., and click **ACCEPT**.
   - **Step 9:** Log back in as **"AHMED K."**, go to Home, and click **START JOB**. Check off the tasks.
   - **Step 10:** Once Ahmed finishes, log back in as **"JOHN DOE"** to pay and rate the service!

## Project Structure
- `ui/theme/`: Custom dark luxury color palette and Material3 theme.
- `ui/components/`: Reusable, pill-shaped UI components (Buttons, Cards, Text).
- `ui/screens/AppState.kt`: Global source of truth for the marketplace data.
- `ui/screens/CustomerScreens.kt`: UI for service requests, offers, tracking, and history.
- `ui/screens/CleanerScreens.kt`: UI for browsing jobs, checklists, and earnings history.
- `ui/Navigation.kt`: Navigation graph, bottom bar logic, and role-based routing.
