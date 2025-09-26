package com.example.smart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart.data.model.Expense
import com.example.smart.data.repository.FirestoreRepository
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val repository: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    private val _expenses = MutableLiveData<List<Expense>>()
    val expenses: LiveData<List<Expense>> get() = _expenses

    fun loadExpenses(userId: String) {
        viewModelScope.launch {
            val data = repository.getExpenses(userId)
            _expenses.postValue(data)
        }
    }

    fun addExpense(userId: String, expense: Expense) {
        viewModelScope.launch {
            repository.addExpense(userId, expense)
            loadExpenses(userId)
        }
    }

   // fun updateExpense(userId: String, expense: Expense) {
    //    viewModelScope.launch {
   //         repository.updateExpense(userId, expense)
    //        loadExpenses(userId)
    //    }
   // }

    fun deleteExpense(userId: String, expenseId: String) {
        viewModelScope.launch {
            repository.deleteExpense(userId, expenseId)
            loadExpenses(userId)
        }
    }

    //fun getTotals(): Pair<Double, Double> {
    //    val expenses = _expenses.value ?: emptyList()
   //     val totalIncome = expenses.filter { it.type == "income" }.sumOf { it.amount }
   //     val totalExpense = expenses.filter { it.type == "expense" }.sumOf { it.amount }
   //     return Pair(totalIncome, totalExpense)
    //}
}