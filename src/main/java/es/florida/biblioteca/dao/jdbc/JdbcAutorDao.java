package es.florida.biblioteca.dao.jdbc;

import es.florida.biblioteca.dao.AutorDao;
import es.florida.biblioteca.modelo.Autor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcAutorDao implements AutorDao {

    private final JdbcClient jdbc;

    public JdbcAutorDao(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Autor> listarTodos() {
        return jdbc.sql("SELECT id, nombre, nacionalidad, anio_nacimiento FROM autor ORDER BY nombre")
                .query(this::mapear)
                .list();
    }

    @Override
    public Optional<Autor> buscarPorId(int id) {
        return jdbc.sql("SELECT id, nombre, nacionalidad, anio_nacimiento FROM autor WHERE id = :id")
                .param("id", id)
                .query(this::mapear)
                .optional();
    }

    @Override
    public Autor guardar(Autor autor) {
        KeyHolder clave = new GeneratedKeyHolder();
        jdbc.sql("INSERT INTO autor (nombre, nacionalidad, anio_nacimiento) VALUES (:nombre, :nacionalidad, :anio)")
                .param("nombre", autor.getNombre())
                .param("nacionalidad", autor.getNacionalidad())
                .param("anio", autor.getAnioNacimiento())
                .update(clave, "id");
        autor.setId(clave.getKey().intValue());
        return autor;
    }

    @Override
    public void actualizar(int id, Autor autor) {
        jdbc.sql("UPDATE autor SET nombre = :nombre, nacionalidad = :nacionalidad, anio_nacimiento = :anio WHERE id = :id")
                .param("nombre", autor.getNombre())
                .param("nacionalidad", autor.getNacionalidad())
                .param("anio", autor.getAnioNacimiento())
                .param("id", id)
                .update();
    }

    @Override
    public boolean eliminar(int id) {
        return jdbc.sql("DELETE FROM autor WHERE id = :id")
                .param("id", id)
                .update() > 0;
    }

    @Override
    public boolean tieneLibros(int id) {
        return jdbc.sql("SELECT COUNT(*) FROM libro WHERE autor_id = :id")
                .param("id", id)
                .query(Integer.class)
                .single() > 0;
    }

    private Autor mapear(ResultSet rs, int fila) throws SQLException {
        return new Autor(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("nacionalidad"),
                rs.getObject("anio_nacimiento") != null ? rs.getShort("anio_nacimiento") : null
        );
    }
}
