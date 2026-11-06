package es.florida.biblioteca.web;

import es.florida.biblioteca.modelo.Libro;
import es.florida.biblioteca.servicio.LibroServicio;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroControlador {

    private final LibroServicio libroServicio;

    public LibroControlador(LibroServicio libroServicio) {
        this.libroServicio = libroServicio;
    }

    @GetMapping
    public List<Libro> buscar(@RequestParam(required = false) String titulo,
                              @RequestParam(required = false) String genero,
                              @RequestParam(required = false) Integer autorId) {
        return libroServicio.buscar(titulo, genero, autorId);
    }

    @GetMapping("/{id}")
    public Libro obtener(@PathVariable int id) {
        return libroServicio.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Libro> crear(@Valid @RequestBody Libro libro) {
        Libro creado = libroServicio.crear(libro);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public Libro actualizar(@PathVariable int id, @Valid @RequestBody Libro libro) {
        return libroServicio.actualizar(id, libro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        libroServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
