package com.example.smart.data.model

data class Transaction(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val amount: Double = 0.0,
    val type: String = "", // "income" o "gasto"
    val date: Long = System.currentTimeMillis()
)