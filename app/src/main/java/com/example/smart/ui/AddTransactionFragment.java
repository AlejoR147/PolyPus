package com.example.smart.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smart.R;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smart.R;
import com.example.smart.data.model.Transaction;
import com.example.smart.viewmodel.TransactionViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class AddTransactionFragment extends Fragment {

    private EditText etTitle, etDescription, etAmount;
    private RadioButton rbIncome, rbExpense;
    private Spinner spCategory;
    private Button btnSave;
    private TransactionViewModel viewModel;

    private final String[] categories = {
            "Alimentación",
            "Transporte",
            "Entretenimiento",
            "Educación",
            "Salud",
            "Ropa",
            "Vivienda",
            "Ahorro",
            "Otro"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // 🔹 Inicializar vistas
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        etAmount = view.findViewById(R.id.etAmount);
        spCategory = view.findViewById(R.id.spCategory);
        rbIncome = view.findViewById(R.id.rbIncome);
        rbExpense = view.findViewById(R.id.rbExpense);
        btnSave = view.findViewById(R.id.btnSave);

        // 🔹 Configurar Spinner de categorías
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        // 🔹 ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);

        // 🔹 Acción del botón
        btnSave.setOnClickListener(v -> saveTransaction());

        return view;
    }

    /** 🔹 Guardar transacción en Firestore */
    private void saveTransaction() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();
        String amountStr = etAmount.getText().toString().trim();
        String type = rbIncome.isChecked() ? "income" : (rbExpense.isChecked() ? "gasto" : "");

        // Validaciones
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(type)) {
            Toast.makeText(getContext(), "Por favor completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Monto inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔹 Crear objeto Transaction
        Transaction transaction = new Transaction(
                "", // Firestore asigna ID
                title,
                description,
                category,
                amount,
                type,
                System.currentTimeMillis()
        );

        // 🔹 Obtener UID del usuario
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 🔹 Guardar en Firestore
        viewModel.addTransaction(userId, transaction);
        Toast.makeText(getContext(), "Transacción guardada exitosamente", Toast.LENGTH_SHORT).show();

        // 🔹 Limpiar campos
        etTitle.setText("");
        etDescription.setText("");
        etAmount.setText("");
        rbIncome.setChecked(false);
        rbExpense.setChecked(false);
        spCategory.setSelection(0);
    }
}