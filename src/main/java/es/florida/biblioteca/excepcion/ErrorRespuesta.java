package es.florida.biblioteca.excepcion;

import java.time.OffsetDateTime;
import java.util.List;

public class ErrorRespuesta {

    private OffsetDateTime marcaTemporal;
    private int estado;
    private String error;
    private String mensaje;
    private String ruta;
    private List<String> detalles;

    public ErrorRespuesta(int estado, String error, String mensaje, String ruta, List<String> detalles) {
        this.marcaTemporal = OffsetDateTime.now();
        this.estado = estado;
        this.error = error;
        this.mensaje = mensaje;
        this.ruta = ruta;
        this.detalles = detalles;
    }

    public OffsetDateTime getMarcaTemporal() { return marcaTemporal; }
    public int getEstado() { return estado; }
    public String getError() { return error; }
    public String getMensaje() { return mensaje; }
    public String getRuta() { return ruta; }
    public List<String> getDetalles() { return detalles; }
}
