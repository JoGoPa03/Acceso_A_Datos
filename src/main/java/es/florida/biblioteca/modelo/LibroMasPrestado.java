package es.florida.biblioteca.modelo;

public class LibroMasPrestado {

    private Integer id;
    private String titulo;
    private Long prestamos;

    public LibroMasPrestado() {}

    public LibroMasPrestado(Integer id, String titulo, Long prestamos) {
        this.id = id;
        this.titulo = titulo;
        this.prestamos = prestamos;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Long getPrestamos() { return prestamos; }
    public void setPrestamos(Long prestamos) { this.prestamos = prestamos; }
}
