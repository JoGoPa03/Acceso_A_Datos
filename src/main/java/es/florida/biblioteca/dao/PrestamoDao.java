package es.florida.biblioteca.dao;

import es.florida.biblioteca.modelo.Prestamo;
import java.util.List;
import java.util.Optional;

public interface PrestamoDao {
    List<Prestamo> listar(String estado);
    Optional<Prestamo> buscarPorId(int id);
    List<Prestamo> buscarPorSocioId(int socioId);
    Prestamo guardar(Prestamo prestamo);
    void registrarDevolucion(int id);
    int contarAbiertos(int socioId);
    boolean tieneVencidos(int socioId);
    boolean tienePrestamoAbiertoDelLibro(int socioId, int libroId);
    int contarPrestamosAbiertosDelLibro(int libroId);
}
