package es.florida.biblioteca.dao;

import es.florida.biblioteca.modelo.Libro;
import es.florida.biblioteca.modelo.LibroMasPrestado;
import java.util.List;
import java.util.Optional;

public interface LibroDao {
    List<Libro> buscar(String titulo, String genero, Integer autorId);
    Optional<Libro> buscarPorId(int id);
    List<Libro> buscarPorAutorId(int autorId);
    Libro guardar(Libro libro);
    void actualizar(int id, Libro libro);
    boolean eliminar(int id);
    boolean tienePrestamoHistorico(int id);
    int contarPrestamosAbiertos(int libroId);
    List<LibroMasPrestado> masPrestados(int limite);
}
