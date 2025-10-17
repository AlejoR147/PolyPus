package com.example.smart.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

    public comunidadFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comunidad, container, false);
        LinearLayout containerLayout = view.findViewById(R.id.videoContainer);

        // 🧾 Lista de videos: título + URL
        String[][] videos = {
                {"Aprende Kotlin desde cero", "https://www.youtube.com/watch?v=MZRmNCgzl3E"},
                {"Introducción a Android Studio", "https://youtu.be/Ks-_Mh1QhMc"},
                {"Conceptos básicos de programación", "https://www.youtube.com/watch?v=ScMzIvxBSi4"}
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
            card.setPadding(16, 16, 16, 16);
            card.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

            // 🖼️ Miniatura
            ImageView thumbnail = new ImageView(requireContext());
            thumbnail.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 600));
            thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Glide.with(this)
                    .load(thumbnailUrl)
                    .into(thumbnail);

            // 📝 Título
            TextView titleView = new TextView(requireContext());
            titleView.setText(title);
            titleView.setTextSize(18);
            titleView.setPadding(0, 16, 0, 16);
            titleView.setTextColor(getResources().getColor(android.R.color.black));

            // ▶️ WebView del video
            WebView webView = new WebView(requireContext());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            LinearLayout.LayoutParams webParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    600
            );
            webView.setLayoutParams(webParams);

            String videoHtml = "<iframe width=\"100%\" height=\"100%\" src=\"" + embedUrl + "\" " +
                    "frameborder=\"0\" allowfullscreen></iframe>";

            webView.loadData(videoHtml, "text/html", "utf-8");

            // Agregar elementos a la tarjeta
            card.addView(thumbnail);
            card.addView(titleView);
            card.addView(webView);

            // Agregar tarjeta al contenedor principal
            containerLayout.addView(card);
        }

        return view;
    }

    // Convierte a formato "embed"
    private String toEmbedUrl(String url) {
        if (url.contains("watch?v=")) {
            return url.replace("watch?v=", "embed/");
        } else if (url.contains("youtu.be/")) {
            return url.replace("youtu.be/", "www.youtube.com/embed/");
        } else {
            return url;
        }
    }

    // Obtiene miniatura a partir del ID del video
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
