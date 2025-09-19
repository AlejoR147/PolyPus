package com.example.smart.data.model

import com.google.firebase.firestore.DocumentId

data class Expense(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val category: String = "",
    val amount: Double = 0.0,
    val date: Long = System.currentTimeMillis()
)