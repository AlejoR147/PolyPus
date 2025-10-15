package com.example.smart.ui;

import android.os.Bundle;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatosFragment extends Fragment {

    private PieChart pieChart;
    private TransactionViewModel viewModel;

    public DatosFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_datos, container, false);

        // Referencias UI
        pieChart = view.findViewById(R.id.pieChart);

        // ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);

        // Observar los cambios en las transacciones
        viewModel.getTransactions().observe(getViewLifecycleOwner(), this::updatePieChart);

        return view;
    }

    /** Actualiza el gráfico circular con los datos de Firestore */
    private void updatePieChart(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            pieChart.clear();
            pieChart.setNoDataText("Sin datos disponibles");
            return;
        }

        // Agrupar gastos por categoría
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Transaction t : transactions) {
            if ("gasto".equalsIgnoreCase(t.getType())) {
                categoryTotals.put(t.getCategory(),
                        categoryTotals.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
            }
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

        // Asignar datos al gráfico
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate(); // Refrescar
    }
}