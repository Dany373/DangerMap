package gt.edu.umg.dangermap.BaseDatos.entidades;

public class Reporte {
    private String tipoIncidente;
    private double latitud;
    private double longitud;
    private String imagenRuta;

    public Reporte(String tipoIncidente, double latitud, double longitud, String imagenRuta) {
        this.tipoIncidente = tipoIncidente;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagenRuta = imagenRuta;
    }

    public String getTipoIncidente() {
        return tipoIncidente;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getImagenRuta() {
        return imagenRuta;
    }
}