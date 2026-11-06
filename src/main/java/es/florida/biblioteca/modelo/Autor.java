package es.florida.biblioteca.modelo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Autor {

    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
    private String nombre;

    @Size(max = 60, message = "La nacionalidad no puede superar los 60 caracteres")
    private String nacionalidad;

    private Short anioNacimiento;

    public Autor() {}

    public Autor(Integer id, String nombre, String nacionalidad, Short anioNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.anioNacimiento = anioNacimiento;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public Short getAnioNacimiento() { return anioNacimiento; }
    public void setAnioNacimiento(Short anioNacimiento) { this.anioNacimiento = anioNacimiento; }
}
