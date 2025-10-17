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
import com.example.smart.data.model.Transaction;
import com.example.smart.viewmodel.TransactionViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart.R;
import com.example.smart.data.model.Transaction;
import com.example.smart.viewmodel.TransactionViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class HomeFragment extends Fragment {

    private TransactionViewModel viewModel;
    private TransactionAdapter adapter;
    private String userId;

    private TextView txtTotalIncome;
    private TextView txtTotalExpenses;
    private TextView txtBalance;
    private RecyclerView recyclerTransactions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "testUser";

        viewModel.loadTransactions(userId);
        observeViewModel();
        txtTotalIncome = view.findViewById(R.id.txtTotalIncome);
        txtTotalExpenses = view.findViewById(R.id.txtTotalExpenses);
        txtBalance = view.findViewById(R.id.txtBalance);
        recyclerTransactions = view.findViewById(R.id.recyclerTransactions);

        recyclerTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TransactionAdapter(this::showEditDeleteDialog);
        recyclerTransactions.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "testUser";

        viewModel.loadTransactions(userId);
        observeViewModel();

        return view;
    }

    private void observeViewModel() {
        viewModel.getTransactions().observe(getViewLifecycleOwner(), this::updateList);
        viewModel.getTotalIncome().observe(getViewLifecycleOwner(),
                value -> txtTotalIncome.setText("$" + value));
        viewModel.getTotalExpenses().observe(getViewLifecycleOwner(),
                value -> txtTotalExpenses.setText("$" + value));
        viewModel.getBalance().observe(getViewLifecycleOwner(),
                value -> txtBalance.setText("$" + value));

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null)
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    private void updateList(List<Transaction> list) {
        adapter.submitList(list);
    }

    private void showEditDeleteDialog(Transaction transaction) {
        String[] options = {"Editar", "Eliminar"};
        new AlertDialog.Builder(requireContext())
                .setTitle(transaction.getTitle())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) editTransaction(transaction);
                    else deleteTransaction(transaction);
                })
                .show();
    }

    private void editTransaction(Transaction transaction) {
        Toast.makeText(requireContext(), "Editar " + transaction.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void deleteTransaction(Transaction transaction) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar transacción")
                .setMessage("¿Seguro que deseas eliminar '" + transaction.getTitle() + "'?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    viewModel.deleteTransaction(userId, transaction.getId());
                    Toast.makeText(requireContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
}