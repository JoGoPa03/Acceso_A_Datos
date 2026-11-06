package es.florida.biblioteca.web;

import es.florida.biblioteca.excepcion.PeticionIncorrectaException;
import es.florida.biblioteca.modelo.LibroMasPrestado;
import es.florida.biblioteca.servicio.LibroServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/informes")
public class InformeControlador {

    private final LibroServicio libroServicio;

    public InformeControlador(LibroServicio libroServicio) {
        this.libroServicio = libroServicio;
    }

    @GetMapping("/mas-prestados")
    public List<LibroMasPrestado> masPrestados(
            @RequestParam(required = false, defaultValue = "5") int limite) {
        if (limite < 1 || limite > 20) {
            throw new PeticionIncorrectaException("El límite debe estar entre 1 y 20");
        }
        return libroServicio.masPrestados(limite);
    }
}
