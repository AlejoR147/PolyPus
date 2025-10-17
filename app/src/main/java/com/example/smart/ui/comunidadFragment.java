package com.example.smart.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.smart.R;

public class comunidadFragment extends Fragment {

    private TextView textRotatingMessages;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int index = 0;
    private final long intervalo = 10000; // 10 segundos

    // Mensajes personalizados
    private final String[] mensajes = new String[]{
            "No dejes para después lo que ahora puedes hacer",
            "¡Haz que tu dinero trabaje por ti!",
            "¡Aprende antes de invertir!",
            "¡Invierte con inteligencia, no con impulso!",
            "¡Compra con cabeza, no con emoción!",
            "¡Educa tu mente para administrar mejor!",
            "¡Cuida tu dinero como cuidas tu tiempo!",
            "¡Apuesta por proyectos que te generen valor!",
            "¡Prioriza tus metas financieras!",
            "¡Enseña a otros el valor del ahorro!",
            "¡Aprende a decir “no” a los gastos impulsivos!",
            "¡Lleva un registro de tus ingresos y egresos!"
    };

    public comunidadFragment() {
        // Constructor vacío requerido
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comunidad, container, false);

        LinearLayout containerLayout = view.findViewById(R.id.videoContainer);
        textRotatingMessages = view.findViewById(R.id.textRotatingMessages);

        startChangingMessages(); // Iniciar rotación de mensajes

        // 🧾 Lista de videos: título + URL
        String[][] videos = {
                {"Educación Financiera para Principiantes - Cómo funcionan las Finanzas", "https://www.youtube.com/watch?v=X38MGyuc0ds"},
                {"¿Cómo Ahorrar? | Explicado con Bananas", "https://www.youtube.com/watch?v=8rWOJ5WenoM"},
                {"Aprende a gestionar MEJOR tu dinero con LA REGLA 50/30/20", "https://www.youtube.com/watch?v=_bgUUswBttU"},
                {"¡PRUÉBALO POR 11 DIAS! 11 Hábitos que Mejorarán Inmediatamente tus Finanzas Personales - Jack Ma","https://www.youtube.com/watch?v=1G3S0mGM7PU"}
        };

        // 🧱 Agregamos cada video como tarjeta
        for (String[] video : videos) {
            String title = video[0];
            String url = video[1];
            String embedUrl = toEmbedUrl(url);
            String thumbnailUrl = getThumbnailUrl(url);

            // Crear un contenedor vertical para la tarjeta
            LinearLayout card = new LinearLayout(requireContext());
            card.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 20, 0, 20);
            card.setLayoutParams(cardParams);
            card.setPadding(20, 16, 20, 16);
            card.setBackgroundColor(Color.parseColor("#2C2C2C"));


            // 📝 Título
            TextView titleView = new TextView(requireContext());
            titleView.setText(title);
            titleView.setTextSize(18);
            titleView.setPadding(0, 16, 0, 16);
            titleView.setTextColor(getResources().getColor(android.R.color.white));

            // ▶️ WebView del video
            WebView webView = new WebView(requireContext());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            LinearLayout.LayoutParams webParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 600
            );
            webView.setLayoutParams(webParams);

            String videoHtml = "<iframe width=\"100%\" height=\"100%\" src=\"" + embedUrl + "\" " +
                    "frameborder=\"0\" allowfullscreen></iframe>";

            webView.loadData(videoHtml, "text/html", "utf-8");

            // Agregar elementos a la tarjeta
            card.addView(titleView);
            card.addView(webView);

            // Agregar tarjeta al contenedor principal
            containerLayout.addView(card);
        }

        return view;
    }

    /**
     * Inicia la rotación automática de mensajes en el TextView.
     */
    private void startChangingMessages() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                textRotatingMessages.setText(mensajes[index]);
                textRotatingMessages.setAlpha(0f);
                textRotatingMessages.animate().alpha(1f).setDuration(600).start();

                index = (index + 1) % mensajes.length;
                handler.postDelayed(this, intervalo);
            }
        });
    }

    /**
     * Convierte una URL de YouTube en su formato embebido.
     */
    private String toEmbedUrl(String url) {
        if (url.contains("watch?v=")) {
            return url.replace("watch?v=", "embed/");
        } else if (url.contains("youtu.be/")) {
            return url.replace("youtu.be/", "www.youtube.com/embed/");
        } else {
            return url;
        }
    }

    /**
     * Obtiene la miniatura del video de YouTube a partir de su URL.
     */
    private String getThumbnailUrl(String url) {
        String videoId = null;
        if (url.contains("watch?v=")) {
            videoId = url.substring(url.indexOf("watch?v=") + 8);
        } else if (url.contains("youtu.be/")) {
            videoId = url.substring(url.indexOf("youtu.be/") + 9);
        } else if (url.contains("embed/")) {
            videoId = url.substring(url.indexOf("embed/") + 6);
        }

        return "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
    }
}