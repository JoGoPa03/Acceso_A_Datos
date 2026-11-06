package es.florida.biblioteca.dao;

import es.florida.biblioteca.modelo.Socio;
import java.util.List;
import java.util.Optional;

public interface SocioDao {
    List<Socio> listarTodos();
    Optional<Socio> buscarPorId(int id);
    Socio guardar(Socio socio);
    void actualizar(int id, Socio socio);
    boolean eliminar(int id);
    boolean tienePrestamos(int id);
}
