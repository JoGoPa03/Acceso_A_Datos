package es.florida.biblioteca.dao.jdbc;

import es.florida.biblioteca.dao.SocioDao;
import es.florida.biblioteca.modelo.Socio;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcSocioDao implements SocioDao {

    private final JdbcClient jdbc;

    public JdbcSocioDao(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Socio> listarTodos() {
        return jdbc.sql(
                "SELECT s.id, s.nombre, s.email, s.fecha_alta, " +
                "COALESCE(SUM(CASE WHEN p.fecha_devolucion IS NULL THEN 1 ELSE 0 END), 0) AS prestamos_abiertos " +
                "FROM socio s LEFT JOIN prestamo p ON p.socio_id = s.id " +
                "GROUP BY s.id ORDER BY s.nombre")
                .query(this::mapear)
                .list();
    }

    @Override
    public Optional<Socio> buscarPorId(int id) {
        return jdbc.sql(
                "SELECT s.id, s.nombre, s.email, s.fecha_alta, " +
                "COALESCE(SUM(CASE WHEN p.fecha_devolucion IS NULL THEN 1 ELSE 0 END), 0) AS prestamos_abiertos " +
                "FROM socio s LEFT JOIN prestamo p ON p.socio_id = s.id " +
                "WHERE s.id = :id GROUP BY s.id")
                .param("id", id)
                .query(this::mapear)
                .optional();
    }

    @Override
    public Socio guardar(Socio socio) {
        KeyHolder clave = new GeneratedKeyHolder();
        jdbc.sql("INSERT INTO socio (nombre, email, fecha_alta) VALUES (:nombre, :email, CURDATE())")
                .param("nombre", socio.getNombre())
                .param("email", socio.getEmail())
                .update(clave, "id");
        socio.setId(clave.getKey().intValue());
        socio.setFechaAlta(java.time.LocalDate.now());
        return socio;
    }

    @Override
    public void actualizar(int id, Socio socio) {
        jdbc.sql("UPDATE socio SET nombre = :nombre, email = :email WHERE id = :id")
                .param("nombre", socio.getNombre())
                .param("email", socio.getEmail())
                .param("id", id)
                .update();
    }

    @Override
    public boolean eliminar(int id) {
        return jdbc.sql("DELETE FROM socio WHERE id = :id")
                .param("id", id)
                .update() > 0;
    }

    @Override
    public boolean tienePrestamos(int id) {
        return jdbc.sql("SELECT COUNT(*) FROM prestamo WHERE socio_id = :id")
                .param("id", id)
                .query(Integer.class)
                .single() > 0;
    }

    private Socio mapear(ResultSet rs, int fila) throws SQLException {
        Socio s = new Socio();
        s.setId(rs.getInt("id"));
        s.setNombre(rs.getString("nombre"));
        s.setEmail(rs.getString("email"));
        s.setFechaAlta(rs.getDate("fecha_alta").toLocalDate());
        s.setPrestamosAbiertos(rs.getInt("prestamos_abiertos"));
        return s;
    }
}
