package gt.edu.umg.dangermap.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gt.edu.umg.dangermap.BaseDatos.entidades.Reporte;
import gt.edu.umg.dangermap.R;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder> {

    private Context context;
    private ArrayList<Reporte> reportes;

    public ReporteAdapter(Context context, ArrayList<Reporte> reportes) {
        this.context = context;
        this.reportes = reportes;
    }

    public static class ReporteViewHolder extends RecyclerView.ViewHolder {
        public TextView txtIncidente;
        public ImageView imgIncidente;

        public ReporteViewHolder(View itemView) {
            super(itemView);
            txtIncidente = itemView.findViewById(R.id.txtIncidente);
            imgIncidente = itemView.findViewById(R.id.imgIncidente);
        }
    }

    @NonNull
    @Override
    public ReporteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new ReporteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteViewHolder holder, int position) {
        Reporte reporte = reportes.get(position);
        holder.txtIncidente.setText("Incidente: " + reporte.getTipoIncidente() +
                "\nLatitud: " + reporte.getLatitud() +
                "\nLongitud: " + reporte.getLongitud());

        // Cargar la imagen usando Glide
        Glide.with(context)
                .load(reporte.getImagenRuta()) // Cargar la imagen desde la ruta
                .placeholder(R.drawable.placeholder_image) // Imagen de espera mientras carga
                .error(R.drawable.error_image) // Imagen en caso de error
                .into(holder.imgIncidente); // Asignar la imagen al ImageView
    }

    @Override
    public int getItemCount() {
        return reportes.size();
    }
}