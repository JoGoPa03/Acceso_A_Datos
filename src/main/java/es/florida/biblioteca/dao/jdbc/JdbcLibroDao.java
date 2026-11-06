package es.florida.biblioteca.dao.jdbc;

import es.florida.biblioteca.dao.LibroDao;
import es.florida.biblioteca.modelo.Libro;
import es.florida.biblioteca.modelo.LibroMasPrestado;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcLibroDao implements LibroDao {

    private final JdbcClient jdbc;

    public JdbcLibroDao(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Libro> buscar(String titulo, String genero, Integer autorId) {
        var sql = new StringBuilder(
                "SELECT l.id, l.isbn, l.titulo, l.anio_publicacion, l.genero, l.ejemplares, l.autor_id, " +
                "a.nombre AS autor_nombre, " +
                "l.ejemplares - COALESCE(SUM(CASE WHEN p.fecha_devolucion IS NULL THEN 1 ELSE 0 END), 0) AS disponibles " +
                "FROM libro l " +
                "JOIN autor a ON l.autor_id = a.id " +
                "LEFT JOIN prestamo p ON p.libro_id = l.id ");
        var condiciones = new ArrayList<String>();
        var params = new java.util.HashMap<String, Object>();

        if (titulo != null && !titulo.isBlank()) {
            condiciones.add("LOWER(l.titulo) LIKE LOWER(:titulo)");
            params.put("titulo", "%" + titulo + "%");
        }
        if (genero != null && !genero.isBlank()) {
            condiciones.add("l.genero = :genero");
            params.put("genero", genero);
        }
        if (autorId != null) {
            condiciones.add("l.autor_id = :autorId");
            params.put("autorId", autorId);
        }
        if (!condiciones.isEmpty()) {
            sql.append("WHERE ").append(String.join(" AND ", condiciones)).append(" ");
        }
        sql.append("GROUP BY l.id, a.nombre ORDER BY l.titulo");

        var consulta = jdbc.sql(sql.toString());
        params.forEach(consulta::param);
        return consulta.query(this::mapearCompleto).list();
    }

    @Override
    public Optional<Libro> buscarPorId(int id) {
        return jdbc.sql(
                "SELECT l.id, l.isbn, l.titulo, l.anio_publicacion, l.genero, l.ejemplares, l.autor_id, " +
                "a.nombre AS autor_nombre, " +
                "l.ejemplares - COALESCE(SUM(CASE WHEN p.fecha_devolucion IS NULL THEN 1 ELSE 0 END), 0) AS disponibles " +
                "FROM libro l " +
                "JOIN autor a ON l.autor_id = a.id " +
                "LEFT JOIN prestamo p ON p.libro_id = l.id " +
                "WHERE l.id = :id " +
                "GROUP BY l.id, a.nombre")
                .param("id", id)
                .query(this::mapearCompleto)
                .optional();
    }

    @Override
    public List<Libro> buscarPorAutorId(int autorId) {
        return jdbc.sql(
                "SELECT l.id, l.isbn, l.titulo, l.anio_publicacion, l.genero, l.ejemplares, l.autor_id, " +
                "a.nombre AS autor_nombre, " +
                "l.ejemplares - COALESCE(SUM(CASE WHEN p.fecha_devolucion IS NULL THEN 1 ELSE 0 END), 0) AS disponibles " +
                "FROM libro l " +
                "JOIN autor a ON l.autor_id = a.id " +
                "LEFT JOIN prestamo p ON p.libro_id = l.id " +
                "WHERE l.autor_id = :autorId " +
                "GROUP BY l.id, a.nombre ORDER BY l.titulo")
                .param("autorId", autorId)
                .query(this::mapearCompleto)
                .list();
    }

    @Override
    public Libro guardar(Libro libro) {
        KeyHolder clave = new GeneratedKeyHolder();
        jdbc.sql("INSERT INTO libro (isbn, titulo, anio_publicacion, genero, ejemplares, autor_id) " +
                 "VALUES (:isbn, :titulo, :anio, :genero, :ejemplares, :autorId)")
                .param("isbn", libro.getIsbn())
                .param("titulo", libro.getTitulo())
                .param("anio", libro.getAnioPublicacion())
                .param("genero", libro.getGenero())
                .param("ejemplares", libro.getEjemplares())
                .param("autorId", libro.getAutorId())
                .update(clave, "id");
        libro.setId(clave.getKey().intValue());
        return libro;
    }

    @Override
    public void actualizar(int id, Libro libro) {
        jdbc.sql("UPDATE libro SET isbn = :isbn, titulo = :titulo, anio_publicacion = :anio, " +
                 "genero = :genero, ejemplares = :ejemplares, autor_id = :autorId WHERE id = :id")
                .param("isbn", libro.getIsbn())
                .param("titulo", libro.getTitulo())
                .param("anio", libro.getAnioPublicacion())
                .param("genero", libro.getGenero())
                .param("ejemplares", libro.getEjemplares())
                .param("autorId", libro.getAutorId())
                .param("id", id)
                .update();
    }

    @Override
    public boolean eliminar(int id) {
        return jdbc.sql("DELETE FROM libro WHERE id = :id")
                .param("id", id)
                .update() > 0;
    }

    @Override
    public boolean tienePrestamoHistorico(int id) {
        return jdbc.sql("SELECT COUNT(*) FROM prestamo WHERE libro_id = :id")
                .param("id", id)
                .query(Integer.class)
                .single() > 0;
    }

    @Override
    public int contarPrestamosAbiertos(int libroId) {
        return jdbc.sql("SELECT COUNT(*) FROM prestamo WHERE libro_id = :id AND fecha_devolucion IS NULL")
                .param("id", libroId)
                .query(Integer.class)
                .single();
    }

    @Override
    public List<LibroMasPrestado> masPrestados(int limite) {
        return jdbc.sql(
                "SELECT l.id, l.titulo, COUNT(p.id) AS prestamos " +
                "FROM libro l JOIN prestamo p ON p.libro_id = l.id " +
                "GROUP BY l.id, l.titulo " +
                "ORDER BY prestamos DESC, l.titulo " +
                "LIMIT :limite")
                .param("limite", limite)
                .query((rs, fila) -> new LibroMasPrestado(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getLong("prestamos")))
                .list();
    }

    private Libro mapearCompleto(ResultSet rs, int fila) throws SQLException {
        Libro l = new Libro();
        l.setId(rs.getInt("id"));
        l.setIsbn(rs.getString("isbn"));
        l.setTitulo(rs.getString("titulo"));
        l.setAnioPublicacion(rs.getObject("anio_publicacion") != null ? rs.getShort("anio_publicacion") : null);
        l.setGenero(rs.getString("genero"));
        l.setEjemplares(rs.getShort("ejemplares"));
        l.setAutorId(rs.getInt("autor_id"));
        l.setAutorNombre(rs.getString("autor_nombre"));
        l.setDisponibles(rs.getInt("disponibles"));
        return l;
    }
}
