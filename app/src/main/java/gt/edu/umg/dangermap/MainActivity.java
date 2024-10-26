package gt.edu.umg.dangermap;


import android.content.Intent;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // Configuración inicial de los botones
        configurarBotones();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Cada vez que la actividad se reanuda, asegura que el botón Reportar funcione correctamente
        configurarBotones();
    }

    private void configurarBotones() {
        // Botón para hacer reporte
        Button btnReporte = findViewById(R.id.btnReporteIncidente);
        btnReporte.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent); // Esto siempre llevará a la actividad donde generas el reporte
        });

        // Botón para ver historial de incidentes
        Button btnHistorial = findViewById(R.id.btnHistorial);
        btnHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistorialActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Ejemplo: añadir un marcador en una ubicación fija
        LatLng ubicacionEjemplo = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(ubicacionEjemplo).title("Zona peligrosa"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacionEjemplo));

        // Aquí podrás añadir marcadores basados en los reportes desde SQLite
    }
}