package gt.edu.umg.dangermap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gt.edu.umg.dangermap.BaseDatos.DbHelper;
import gt.edu.umg.dangermap.BaseDatos.entidades.Reporte;
import gt.edu.umg.dangermap.adaptadores.ReporteAdapter;

public class HistorialActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHistorial;
    private ArrayList<Reporte> listaReportes;
    private ReporteAdapter reporteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Inicializar el RecyclerView
        recyclerViewHistorial = findViewById(R.id.recyclerViewHistorial);
        listaReportes = new ArrayList<>();

        // Cargar los reportes desde la base de datos
        cargarReportes();

        // Configurar el RecyclerView
        recyclerViewHistorial.setLayoutManager(new LinearLayoutManager(this));
        reporteAdapter = new ReporteAdapter(this, listaReportes);
        recyclerViewHistorial.setAdapter(reporteAdapter);

        // Botón para regresar a la actividad anterior
        Button btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(v -> {
            // Regresar a la MainActivity
            finish(); // Cerrar la actividad actual
        });
    }

    // Método para cargar los reportes desde la base de datos
    private void cargarReportes() {
        SQLiteDatabase db = new DbHelper(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT tipo_incidente, latitud, longitud, imagen_ruta FROM reportes", null);

        if (cursor.moveToFirst()) {
            do {
                String tipoIncidente = cursor.getString(0);
                double latitud = cursor.getDouble(1);
                double longitud = cursor.getDouble(2);
                String imagenRuta = cursor.getString(3); // Obtener la ruta de la imagen

                // Agregar el reporte a la lista
                listaReportes.add(new Reporte(tipoIncidente, latitud, longitud, imagenRuta));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}