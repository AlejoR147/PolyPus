package com.example.smart.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart.R;
import com.example.smart.data.model.Expense;
import com.example.smart.viewmodel.ExpenseViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private ExpenseViewModel viewModel;
    //private ExpenseAdapter adapter;
    private final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(); // ⚠️ luego reemplazar por FirebaseAuth.getInstance().getCurrentUser().getUid()

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        TextView totalIncomeView = view.findViewById(R.id.totalIncome);
        TextView totalExpenseView = view.findViewById(R.id.totalExpense);

        //adapter = new ExpenseAdapter(expense -> {
       //    showEditDeleteDialog(expense);
       //     return null; // porque en Java las lambdas necesitan retorno si es un SAM
       // });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
       // recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        //viewModel.getExpenses().observe(getViewLifecycleOwner(), list -> {
        //    adapter.submitList(list);
//
        //    double[] totals = viewModel.getTotals();
       //     double income = totals[0];
      //      double expense = totals[1];
//
      //      totalIncomeView.setText("Ingresos: " + income);
     //       totalExpenseView.setText("Gastos: " + expense);
    //    });

        viewModel.loadExpenses(userId);

        return view;
    }

    private void showEditDeleteDialog(Expense expense) {
        new AlertDialog.Builder(requireContext())
                .setTitle(expense.getTitle())
                .setMessage("¿Qué deseas hacer con este registro?")
                .setPositiveButton("Editar", (dialog, which) -> {
                    // Navegar a AddTransactionFragment con datos del gasto
                })
                .setNegativeButton("Eliminar", (dialog, which) -> {
                    if (expense.getId() != null) {
                        viewModel.deleteExpense(userId, expense.getId());
                    }
                })
                .setNeutralButton("Cancelar", null)
                .show();
    }
}