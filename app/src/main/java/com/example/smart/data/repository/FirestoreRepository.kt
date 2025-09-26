package com.example.smart.data.repository

import com.example.smart.data.model.Expense
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = Firebase.firestore

    private fun expensesRef(uid: String) =
        db.collection("testusers").document(uid).collection("expense")

    suspend fun addExpense(userId: String, expense: Expense): String {
        val ref = expensesRef(userId).add(expense).await()
        return ref.id
    }

    suspend fun updateExpense(userId: String, expenseId: String, expense: Expense) {
        expensesRef(userId).document(expenseId).set(expense).await()
    }

    suspend fun deleteExpense(userId: String, expenseId: String) {
        expensesRef(userId).document(expenseId).delete().await()
    }

    suspend fun getExpenses(userId: String): List<Expense> {
        val snap = expensesRef(userId)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()
        return snap.toObjects(Expense::class.java)
    }

    // listener en tiempo real (útil si quieres actualizar automático)
    fun listenExpenses(userId: String, onUpdate: (List<Expense>) -> Unit): ListenerRegistration {
        return expensesRef(userId)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, error ->
                if (error != null) return@addSnapshotListener
                val list = snap?.toObjects(Expense::class.java) ?: emptyList()
                onUpdate(list)
            }
    }

}