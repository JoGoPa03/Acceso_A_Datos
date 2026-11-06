package es.florida.biblioteca.dao.jdbc;

import es.florida.biblioteca.dao.PrestamoDao;
import es.florida.biblioteca.modelo.Prestamo;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcPrestamoDao implements PrestamoDao {

    private final JdbcClient jdbc;

    public JdbcPrestamoDao(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Prestamo> listar(String estado) {
        var sql = new StringBuilder(
                "SELECT id, libro_id, socio_id, fecha_prestamo, fecha_limite, fecha_devolucion FROM prestamo");
        if (estado != null) {
            switch (estado.toUpperCase()) {
                case "DEVUELTO" -> sql.append(" WHERE fecha_devolucion IS NOT NULL");
                case "ACTIVO" -> sql.append(" WHERE fecha_devolucion IS NULL AND fecha_limite >= CURDATE()");
                case "VENCIDO" -> sql.append(" WHERE fecha_devolucion IS NULL AND fecha_limite < CURDATE()");
            }
        }
        sql.append(" ORDER BY fecha_prestamo DESC");
        return jdbc.sql(sql.toString()).query(this::mapear).list();
    }

    @Override
    public Optional<Prestamo> buscarPorId(int id) {
        return jdbc.sql("SELECT id, libro_id, socio_id, fecha_prestamo, fecha_limite, fecha_devolucion FROM prestamo WHERE id = :id")
                .param("id", id)
                .query(this::mapear)
                .optional();
    }

    @Override
    public List<Prestamo> buscarPorSocioId(int socioId) {
        return jdbc.sql("SELECT id, libro_id, socio_id, fecha_prestamo, fecha_limite, fecha_devolucion FROM prestamo WHERE socio_id = :socioId ORDER BY fecha_prestamo DESC")
                .param("socioId", socioId)
                .query(this::mapear)
                .list();
    }

    @Override
    public Prestamo guardar(Prestamo prestamo) {
        KeyHolder clave = new GeneratedKeyHolder();
        jdbc.sql("INSERT INTO prestamo (libro_id, socio_id, fecha_prestamo, fecha_limite) VALUES (:libroId, :socioId, :fechaPrestamo, :fechaLimite)")
                .param("libroId", prestamo.getLibroId())
                .param("socioId", prestamo.getSocioId())
                .param("fechaPrestamo", prestamo.getFechaPrestamo())
                .param("fechaLimite", prestamo.getFechaLimite())
                .update(clave, "id");
        prestamo.setId(clave.getKey().intValue());
        return prestamo;
    }

    @Override
    public void registrarDevolucion(int id) {
        jdbc.sql("UPDATE prestamo SET fecha_devolucion = CURDATE() WHERE id = :id")
                .param("id", id)
                .update();
    }

    @Override
    public int contarAbiertos(int socioId) {
        return jdbc.sql("SELECT COUNT(*) FROM prestamo WHERE socio_id = :socioId AND fecha_devolucion IS NULL")
                .param("socioId", socioId)
                .query(Integer.class)
                .single();
    }

    @Override
    public boolean tieneVencidos(int socioId) {
        return jdbc.sql("SELECT COUNT(*) FROM prestamo WHERE socio_id = :socioId AND fecha_devolucion IS NULL AND fecha_limite < CURDATE()")
                .param("socioId", socioId)
                .query(Integer.class)
                .single() > 0;
    }

    @Override
    public boolean tienePrestamoAbiertoDelLibro(int socioId, int libroId) {
        return jdbc.sql("SELECT COUNT(*) FROM prestamo WHERE socio_id = :socioId AND libro_id = :libroId AND fecha_devolucion IS NULL")
                .param("socioId", socioId)
                .param("libroId", libroId)
                .query(Integer.class)
                .single() > 0;
    }

    @Override
    public int contarPrestamosAbiertosDelLibro(int libroId) {
        return jdbc.sql("SELECT COUNT(*) FROM prestamo WHERE libro_id = :libroId AND fecha_devolucion IS NULL")
                .param("libroId", libroId)
                .query(Integer.class)
                .single();
    }

    private Prestamo mapear(ResultSet rs, int fila) throws SQLException {
        Prestamo p = new Prestamo();
        p.setId(rs.getInt("id"));
        p.setLibroId(rs.getInt("libro_id"));
        p.setSocioId(rs.getInt("socio_id"));
        p.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
        p.setFechaLimite(rs.getDate("fecha_limite").toLocalDate());
        var dev = rs.getDate("fecha_devolucion");
        p.setFechaDevolucion(dev != null ? dev.toLocalDate() : null);
        if (p.getFechaDevolucion() != null) {
            p.setEstado("DEVUELTO");
        } else if (p.getFechaLimite().isBefore(LocalDate.now())) {
            p.setEstado("VENCIDO");
        } else {
            p.setEstado("ACTIVO");
        }
        return p;
    }
}
