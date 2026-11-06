package es.florida.biblioteca.servicio;

import es.florida.biblioteca.dao.AutorDao;
import es.florida.biblioteca.excepcion.ConflictoException;
import es.florida.biblioteca.excepcion.RecursoNoEncontradoException;
import es.florida.biblioteca.modelo.Autor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorServicio {

    private final AutorDao autorDao;

    public AutorServicio(AutorDao autorDao) {
        this.autorDao = autorDao;
    }

    public List<Autor> listarTodos() {
        return autorDao.listarTodos();
    }

    public Autor buscarPorId(int id) {
        return autorDao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el autor " + id));
    }

    public Autor crear(Autor autor) {
        return autorDao.guardar(autor);
    }

    public Autor actualizar(int id, Autor autor) {
        autorDao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el autor " + id));
        autorDao.actualizar(id, autor);
        return autorDao.buscarPorId(id).orElseThrow();
    }

    public void eliminar(int id) {
        autorDao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el autor " + id));
        if (autorDao.tieneLibros(id)) {
            throw new ConflictoException("No se puede eliminar el autor " + id + " porque tiene libros asociados");
        }
        autorDao.eliminar(id);
    }
}
