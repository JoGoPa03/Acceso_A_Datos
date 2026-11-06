package es.florida.biblioteca.servicio;

import es.florida.biblioteca.dao.AutorDao;
import es.florida.biblioteca.dao.LibroDao;
import es.florida.biblioteca.excepcion.ConflictoException;
import es.florida.biblioteca.excepcion.PeticionIncorrectaException;
import es.florida.biblioteca.excepcion.RecursoNoEncontradoException;
import es.florida.biblioteca.modelo.Libro;
import es.florida.biblioteca.modelo.LibroMasPrestado;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroServicio {

    private final LibroDao libroDao;
    private final AutorDao autorDao;

    public LibroServicio(LibroDao libroDao, AutorDao autorDao) {
        this.libroDao = libroDao;
        this.autorDao = autorDao;
    }

    public List<Libro> buscar(String titulo, String genero, Integer autorId) {
        return libroDao.buscar(titulo, genero, autorId);
    }

    public Libro buscarPorId(int id) {
        return libroDao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el libro " + id));
    }

    public List<Libro> buscarPorAutorId(int autorId) {
        autorDao.buscarPorId(autorId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el autor " + autorId));
        return libroDao.buscarPorAutorId(autorId);
    }

    public Libro crear(Libro libro) {
        autorDao.buscarPorId(libro.getAutorId())
                .orElseThrow(() -> new PeticionIncorrectaException("No existe el autor " + libro.getAutorId()));
        try {
            return libroDao.guardar(libro);
        } catch (DuplicateKeyException e) {
            throw new ConflictoException("Ya existe un libro con el ISBN " + libro.getIsbn());
        }
    }

    public Libro actualizar(int id, Libro libro) {
        Libro existente = libroDao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el libro " + id));
        autorDao.buscarPorId(libro.getAutorId())
                .orElseThrow(() -> new PeticionIncorrectaException("No existe el autor " + libro.getAutorId()));
        int abiertos = libroDao.contarPrestamosAbiertos(id);
        if (libro.getEjemplares() < abiertos) {
            throw new ConflictoException("Los ejemplares (" + libro.getEjemplares() +
                    ") no pueden ser menos que los préstamos abiertos (" + abiertos + ")");
        }
        try {
            libroDao.actualizar(id, libro);
        } catch (DuplicateKeyException e) {
            throw new ConflictoException("Ya existe un libro con el ISBN " + libro.getIsbn());
        }
        return libroDao.buscarPorId(id).orElseThrow();
    }

    public void eliminar(int id) {
        libroDao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el libro " + id));
        if (libroDao.tienePrestamoHistorico(id)) {
            throw new ConflictoException("No se puede eliminar el libro " + id + " porque tiene préstamos registrados");
        }
        libroDao.eliminar(id);
    }

    public List<LibroMasPrestado> masPrestados(int limite) {
        return libroDao.masPrestados(limite);
    }
}
