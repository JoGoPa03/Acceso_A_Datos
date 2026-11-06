package es.florida.biblioteca.servicio;

import es.florida.biblioteca.dao.SocioDao;
import es.florida.biblioteca.excepcion.ConflictoException;
import es.florida.biblioteca.excepcion.RecursoNoEncontradoException;
import es.florida.biblioteca.modelo.Socio;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioServicio {

    private final SocioDao socioDao;

    public SocioServicio(SocioDao socioDao) {
        this.socioDao = socioDao;
    }

    public List<Socio> listarTodos() {
        return socioDao.listarTodos();
    }

    public Socio buscarPorId(int id) {
        return socioDao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el socio " + id));
    }

    public Socio crear(Socio socio) {
        try {
            return socioDao.guardar(socio);
        } catch (DuplicateKeyException e) {
            throw new ConflictoException("Ya existe un socio con el email " + socio.getEmail());
        }
    }

    public Socio actualizar(int id, Socio socio) {
        socioDao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el socio " + id));
        try {
            socioDao.actualizar(id, socio);
        } catch (DuplicateKeyException e) {
            throw new ConflictoException("Ya existe un socio con el email " + socio.getEmail());
        }
        return socioDao.buscarPorId(id).orElseThrow();
    }

    public void eliminar(int id) {
        socioDao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el socio " + id));
        if (socioDao.tienePrestamos(id)) {
            throw new ConflictoException("No se puede eliminar el socio " + id + " porque tiene préstamos registrados");
        }
        socioDao.eliminar(id);
    }
}
