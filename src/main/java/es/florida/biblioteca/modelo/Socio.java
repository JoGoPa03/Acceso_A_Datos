package es.florida.biblioteca.modelo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class Socio {

    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 160, message = "El email no puede superar los 160 caracteres")
    private String email;

    private LocalDate fechaAlta;

    private Integer prestamosAbiertos;

    public Socio() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }
    public Integer getPrestamosAbiertos() { return prestamosAbiertos; }
    public void setPrestamosAbiertos(Integer prestamosAbiertos) { this.prestamosAbiertos = prestamosAbiertos; }
}
