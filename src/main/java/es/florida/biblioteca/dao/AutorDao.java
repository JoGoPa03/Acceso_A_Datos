package es.florida.biblioteca.dao;

import es.florida.biblioteca.modelo.Autor;
import java.util.List;
import java.util.Optional;

public interface AutorDao {
    List<Autor> listarTodos();
    Optional<Autor> buscarPorId(int id);
    Autor guardar(Autor autor);
    void actualizar(int id, Autor autor);
    boolean eliminar(int id);
    boolean tieneLibros(int id);
}
