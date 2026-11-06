package es.florida.biblioteca.web;

import es.florida.biblioteca.modelo.Prestamo;
import es.florida.biblioteca.modelo.Socio;
import es.florida.biblioteca.servicio.PrestamoServicio;
import es.florida.biblioteca.servicio.SocioServicio;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/socios")
public class SocioControlador {

    private final SocioServicio socioServicio;
    private final PrestamoServicio prestamoServicio;

    public SocioControlador(SocioServicio socioServicio, PrestamoServicio prestamoServicio) {
        this.socioServicio = socioServicio;
        this.prestamoServicio = prestamoServicio;
    }

    @GetMapping
    public List<Socio> listar() {
        return socioServicio.listarTodos();
    }

    @GetMapping("/{id}")
    public Socio obtener(@PathVariable int id) {
        return socioServicio.buscarPorId(id);
    }

    @GetMapping("/{id}/prestamos")
    public List<Prestamo> prestamosDelSocio(@PathVariable int id) {
        socioServicio.buscarPorId(id);
        return prestamoServicio.buscarPorSocioId(id);
    }

    @PostMapping
    public ResponseEntity<Socio> crear(@Valid @RequestBody Socio socio) {
        Socio creado = socioServicio.crear(socio);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public Socio actualizar(@PathVariable int id, @Valid @RequestBody Socio socio) {
        return socioServicio.actualizar(id, socio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        socioServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
