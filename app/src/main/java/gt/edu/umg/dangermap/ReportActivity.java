package gt.edu.umg.dangermap;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import gt.edu.umg.dangermap.BaseDatos.DbHelper;

public class ReportActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 102;
    private static final int CAMERA_REQUEST_CODE = 103;

    private EditText txtTipoIncidente;
    private Button btnGuardarReporte, btnTomarFoto,btnRegresar;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report); // Asegúrate de que el layout esté definido

        txtTipoIncidente = findViewById(R.id.txtTipoIncidente);
        btnGuardarReporte = findViewById(R.id.btnGuardarReporte);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        btnRegresar = findViewById(R.id.btnRegresar); // Inicializa el botón regresar

        // Inicializa ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Restablecer campos
        resetFields();

        btnTomarFoto.setOnClickListener(v -> {
            checkCameraPermission();
        });

        btnGuardarReporte.setOnClickListener(v -> {
            checkLocationPermission(); // Verifica permiso de ubicación al guardar el reporte
        });

        // Configura el botón regresar
        btnRegresar.setOnClickListener(v -> {
            finish();
        });

        // Si necesitas verificar el permiso de almacenamiento al inicio
        checkStoragePermission();
        checkLocationPermission();
    }

    // Método para restablecer campos
    private void resetFields() {
        txtTipoIncidente.setText(""); // Vaciar el campo de texto
        imageUri = null; // Restablecer la imagen a null
        // Aquí puedes restablecer cualquier otro campo o elemento gráfico si es necesario
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Si no se tiene permiso, solicitarlo
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permiso ya concedido, puedes abrir la cámara
            openCamera();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permiso de ubicación
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            obtenerUbicacion(); // Si ya se tiene el permiso, obtener la ubicación
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Si no se tiene permiso, solicitarlo
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, abre la cámara
                openCamera();
            } else {
                Toast.makeText(this, "Permiso para acceder a la cámara denegado", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion(); // Reintenta obtener la ubicación si se concede el permiso
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes proceder
                Toast.makeText(this, "Permiso de almacenamiento concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, "gt.edu.umg.dangermap.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Aquí puedes manejar la imagen capturada
            // Por ejemplo, puedes mostrarla en un ImageView o guardarla según tus necesidades
        }
    }

    private void obtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicita permiso de ubicación si no se ha concedido
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permiso concedido, obtiene la ubicación
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitud = location.getLatitude();
                                double longitud = location.getLongitude();
                                guardarReporte(latitud, longitud); // Llama al método para guardar el reporte
                            } else {
                                Toast.makeText(ReportActivity.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void guardarReporte(double latitud, double longitud) {
        // Guarda en la base de datos SQLite
        SQLiteDatabase db = new DbHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tipo_incidente", txtTipoIncidente.getText().toString());
        values.put("latitud", latitud);
        values.put("longitud", longitud);
        values.put("imagen_ruta", imageUri != null ? imageUri.toString() : ""); // Guarda la ruta de la imagen

        db.insert("reportes", null, values);
        Toast.makeText(this, "Reporte guardado", Toast.LENGTH_SHORT).show();

        // No cerrar la actividad, solo restablecer los campos
        resetFields();
    }
}