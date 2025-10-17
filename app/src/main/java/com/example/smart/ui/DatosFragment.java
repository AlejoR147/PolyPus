package com.example.smart.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smart.R;
import com.example.smart.viewmodel.TransactionViewModel;
import com.example.smart.data.model.Transaction;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatosFragment extends Fragment {

    private PieChart pieChart;
    private TransactionViewModel viewModel;

    public DatosFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos, container, false);

        pieChart = view.findViewById(R.id.pieChart);

        // 🎨 Configuración visual
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.animateY(1400, Easing.EaseInOutQuad);

        // 🧠 ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);

        // ⚙️ Obtener UID del usuario
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = (auth.getCurrentUser() != null)
                ? auth.getCurrentUser().getUid()
                : "testUser";

        // 🔹 Cargar transacciones
        viewModel.loadTransactions(userId);

        // 🔹 Observar cambios
        viewModel.getTransactions().observe(getViewLifecycleOwner(), this::updatePieChart);

        return view;
    }

    /** 🔹 Actualiza el gráfico circular con los datos de gasto agrupados por categoría */
    private void updatePieChart(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            pieChart.clear();
            pieChart.setNoDataText("Sin datos disponibles");
            Log.w("DatosFragment", "No hay transacciones para mostrar.");
            return;
        }

        // Agrupar gastos por categoría
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Transaction t : transactions) {
            Log.d("DatosFragment", "Transacción: " + t.getTitle() + " | Tipo: " + t.getType() + " | Categoría: " + t.getCategory() + " | Monto: " + t.getAmount());

            if (t.getType() != null && t.getType().equalsIgnoreCase("gasto")) {
                String category = t.getCategory() != null ? t.getCategory() : "Sin categoría";
                categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + t.getAmount());
            }
        }

        if (categoryTotals.isEmpty()) {
            pieChart.clear();
            pieChart.setNoDataText("No hay gastos para mostrar");
            Log.w("DatosFragment", "No hay gastos en las transacciones.");
            return;
        }

        // Crear entradas del gráfico
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        // Configurar dataset
        PieDataSet dataSet = new PieDataSet(entries, "Gastos por categoría");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);
        dataSet.setSliceSpace(2f);
        dataSet.setValueTextColor(Color.WHITE);

        // Asignar datos al gráfico
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate(); // 🔄 Refrescar gráfico
    }
}