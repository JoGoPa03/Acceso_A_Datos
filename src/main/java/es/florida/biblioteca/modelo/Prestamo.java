package es.florida.biblioteca.modelo;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class Prestamo {

    private Integer id;

    @NotNull(message = "El libro es obligatorio")
    private Integer libroId;

    @NotNull(message = "El socio es obligatorio")
    private Integer socioId;

    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;
    private String estado;

    public Prestamo() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getLibroId() { return libroId; }
    public void setLibroId(Integer libroId) { this.libroId = libroId; }
    public Integer getSocioId() { return socioId; }
    public void setSocioId(Integer socioId) { this.socioId = socioId; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
