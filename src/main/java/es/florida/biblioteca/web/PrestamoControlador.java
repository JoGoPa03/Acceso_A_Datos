package es.florida.biblioteca.web;

import es.florida.biblioteca.modelo.Prestamo;
import es.florida.biblioteca.servicio.PrestamoServicio;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoControlador {

    private final PrestamoServicio prestamoServicio;

    public PrestamoControlador(PrestamoServicio prestamoServicio) {
        this.prestamoServicio = prestamoServicio;
    }

    @GetMapping
    public List<Prestamo> listar(@RequestParam(required = false) String estado) {
        return prestamoServicio.listar(estado);
    }

    @GetMapping("/{id}")
    public Prestamo obtener(@PathVariable int id) {
        return prestamoServicio.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Prestamo> crear(@Valid @RequestBody Prestamo prestamo) {
        Prestamo creado = prestamoServicio.crear(prestamo);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}/devolucion")
    public Prestamo devolver(@PathVariable int id) {
        return prestamoServicio.devolver(id);
    }
}
