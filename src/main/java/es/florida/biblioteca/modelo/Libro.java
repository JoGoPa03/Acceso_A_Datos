package es.florida.biblioteca.modelo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Libro {

    private Integer id;

    @NotBlank(message = "El ISBN es obligatorio")
    @Pattern(regexp = "\\d{13}", message = "El ISBN debe tener exactamente 13 dígitos")
    private String isbn;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres")
    private String titulo;

    private Short anioPublicacion;

    @Size(max = 40, message = "El género no puede superar los 40 caracteres")
    private String genero;

    @NotNull(message = "El número de ejemplares es obligatorio")
    @Min(value = 1, message = "Debe haber al menos un ejemplar")
    private Short ejemplares;

    @NotNull(message = "El autor es obligatorio")
    private Integer autorId;

    private Integer disponibles;
    private String autorNombre;

    public Libro() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Short getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(Short anioPublicacion) { this.anioPublicacion = anioPublicacion; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public Short getEjemplares() { return ejemplares; }
    public void setEjemplares(Short ejemplares) { this.ejemplares = ejemplares; }
    public Integer getAutorId() { return autorId; }
    public void setAutorId(Integer autorId) { this.autorId = autorId; }
    public Integer getDisponibles() { return disponibles; }
    public void setDisponibles(Integer disponibles) { this.disponibles = disponibles; }
    public String getAutorNombre() { return autorNombre; }
    public void setAutorNombre(String autorNombre) { this.autorNombre = autorNombre; }
}
