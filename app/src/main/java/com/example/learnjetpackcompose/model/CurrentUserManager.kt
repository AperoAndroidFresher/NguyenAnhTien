package com.example.learnjetpackcompose.model

/**
 * Singleton object to manage the current user's ID across the app
 */
object CurrentUserManager {
    // Default to null, meaning no user is logged in
    private var currentUserId: Int? = null

    fun setCurrentUserId(userId: Int) {
        currentUserId = userId
    }

    fun getCurrentUserId(): Int {
        // Return the current user ID or default to 1 if not set
        return currentUserId ?: 1
    }

    fun clearCurrentUserId() {
        currentUserId = null
    }
}