package com.example.smart.viewmodel


import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smart.data.model.Transaction
import com.example.smart.data.repository.FirestoreRepository

class TransactionViewModel(
    private val repository: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    private val _totalIncome = MutableLiveData<Double>(0.0)
    val totalIncome: LiveData<Double> = _totalIncome

    private val _totalExpenses = MutableLiveData<Double>(0.0)
    val totalExpenses: LiveData<Double> = _totalExpenses

    private val _balance = MutableLiveData<Double>(0.0)
    val balance: LiveData<Double> = _balance

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    /** 🔹 Cargar transacciones desde Firestore */
    fun loadTransactions(userId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val list = repository.getTransactions(userId)
                _transactions.value = list
                calculateTotals(list)
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    /** 🔹 Agregar una nueva transacción */
    fun addTransaction(userId: String, transaction: Transaction) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repository.addTransaction(userId, transaction)
                loadTransactions(userId) // refrescar datos
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    /** 🔹 Actualizar una transacción existente */
    fun updateTransaction(userId: String, transactionId: String, transaction: Transaction) {
        viewModelScope.launch {
            try {
                repository.updateTransaction(userId, transactionId, transaction)
                loadTransactions(userId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** 🔹 Eliminar una transacción */
    fun deleteTransaction(userId: String, transactionId: String) {
        viewModelScope.launch {
            try {
                repository.deleteTransaction(userId, transactionId)
                loadTransactions(userId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** 🔹 Calcular totales de ingresos, gastos y balance */
    private fun calculateTotals(list: List<Transaction>) {
        val income = list.filter { it.type == "income" }.sumOf { it.amount }
        val expenses = list.filter { it.type == "gasto" }.sumOf { it.amount }
        val balanceCalc = income - expenses

        _totalIncome.value = income
        _totalExpenses.value = expenses
        _balance.value = balanceCalc
    }
}