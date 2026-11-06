package es.florida.biblioteca.servicio;

import es.florida.biblioteca.dao.PrestamoDao;
import es.florida.biblioteca.excepcion.ConflictoException;
import es.florida.biblioteca.excepcion.PeticionIncorrectaException;
import es.florida.biblioteca.excepcion.RecursoNoEncontradoException;
import es.florida.biblioteca.modelo.Prestamo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoServicio {

    private final PrestamoDao prestamoDao;
    private final JdbcClient jdbc;
    private final int prestamoMaximo;
    private final int prestamoDias;

    public PrestamoServicio(PrestamoDao prestamoDao, JdbcClient jdbc,
                            @Value("${biblioteca.prestamo.maximo}") int prestamoMaximo,
                            @Value("${biblioteca.prestamo.dias}") int prestamoDias) {
        this.prestamoDao = prestamoDao;
        this.jdbc = jdbc;
        this.prestamoMaximo = prestamoMaximo;
        this.prestamoDias = prestamoDias;
    }

    public List<Prestamo> listar(String estado) {
        if (estado != null && !estado.isBlank()) {
            String upper = estado.toUpperCase();
            if (!upper.equals("ACTIVO") && !upper.equals("VENCIDO") && !upper.equals("DEVUELTO")) {
                throw new PeticionIncorrectaException("Estado no válido: " + estado + ". Valores admitidos: ACTIVO, VENCIDO, DEVUELTO");
            }
        }
        return prestamoDao.listar(estado);
    }

    public Prestamo buscarPorId(int id) {
        return prestamoDao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el préstamo " + id));
    }

    public List<Prestamo> buscarPorSocioId(int socioId) {
        return prestamoDao.buscarPorSocioId(socioId);
    }

    @Transactional
    public Prestamo crear(Prestamo prestamo) {
        int libroId = prestamo.getLibroId();
        int socioId = prestamo.getSocioId();

        // Bloquear libro y verificar existencia
        var libro = jdbc.sql("SELECT id, ejemplares FROM libro WHERE id = :id FOR UPDATE")
                .param("id", libroId)
                .query((rs, f) -> new int[]{rs.getInt("id"), rs.getInt("ejemplares")})
                .optional()
                .orElseThrow(() -> new PeticionIncorrectaException("No existe el libro " + libroId));

        // Bloquear socio y verificar existencia
        jdbc.sql("SELECT id FROM socio WHERE id = :id FOR UPDATE")
                .param("id", socioId)
                .query(Integer.class)
                .optional()
                .orElseThrow(() -> new PeticionIncorrectaException("No existe el socio " + socioId));

        // Comprobar vencidos
        if (prestamoDao.tieneVencidos(socioId)) {
            throw new ConflictoException("El socio " + socioId + " tiene préstamos vencidos");
        }

        // Comprobar máximo de préstamos abiertos
        int abiertos = prestamoDao.contarAbiertos(socioId);
        if (abiertos >= prestamoMaximo) {
            throw new ConflictoException("El socio " + socioId + " ha alcanzado el máximo de " + prestamoMaximo + " préstamos abiertos");
        }

        // Comprobar duplicado
        if (prestamoDao.tienePrestamoAbiertoDelLibro(socioId, libroId)) {
            throw new ConflictoException("El socio " + socioId + " ya tiene un préstamo abierto del libro " + libroId);
        }

        // Comprobar disponibilidad
        int prestamosDelLibro = prestamoDao.contarPrestamosAbiertosDelLibro(libroId);
        if (prestamosDelLibro >= libro[1]) {
            throw new ConflictoException("No quedan ejemplares disponibles del libro " + libroId);
        }

        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaLimite(LocalDate.now().plusDays(prestamoDias));
        prestamo.setEstado("ACTIVO");
        return prestamoDao.guardar(prestamo);
    }

    public Prestamo devolver(int id) {
        Prestamo prestamo = prestamoDao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el préstamo " + id));
        if (prestamo.getFechaDevolucion() != null) {
            throw new ConflictoException("El préstamo " + id + " ya fue devuelto");
        }
        prestamoDao.registrarDevolucion(id);
        return prestamoDao.buscarPorId(id).orElseThrow();
    }
}
