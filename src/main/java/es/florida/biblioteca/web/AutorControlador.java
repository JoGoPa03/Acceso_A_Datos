package es.florida.biblioteca.web;

import es.florida.biblioteca.modelo.Autor;
import es.florida.biblioteca.modelo.Libro;
import es.florida.biblioteca.servicio.AutorServicio;
import es.florida.biblioteca.servicio.LibroServicio;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorControlador {

    private final AutorServicio autorServicio;
    private final LibroServicio libroServicio;

    public AutorControlador(AutorServicio autorServicio, LibroServicio libroServicio) {
        this.autorServicio = autorServicio;
        this.libroServicio = libroServicio;
    }

    @GetMapping
    public List<Autor> listar() {
        return autorServicio.listarTodos();
    }

    @GetMapping("/{id}")
    public Autor obtener(@PathVariable int id) {
        return autorServicio.buscarPorId(id);
    }

    @GetMapping("/{id}/libros")
    public List<Libro> librosDelAutor(@PathVariable int id) {
        return libroServicio.buscarPorAutorId(id);
    }

    @PostMapping
    public ResponseEntity<Autor> crear(@Valid @RequestBody Autor autor) {
        Autor creado = autorServicio.crear(autor);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public Autor actualizar(@PathVariable int id, @Valid @RequestBody Autor autor) {
        return autorServicio.actualizar(id, autor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        autorServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
