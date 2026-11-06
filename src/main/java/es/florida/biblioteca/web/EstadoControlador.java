package es.florida.biblioteca.web;

import es.florida.biblioteca.config.BibliotecaConfiguracion;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class EstadoControlador {

    private final JdbcClient jdbc;
    private final BibliotecaConfiguracion config;

    public EstadoControlador(JdbcClient jdbc, BibliotecaConfiguracion config) {
        this.jdbc = jdbc;
        this.config = config;
    }

    @GetMapping("/api/estado")
    public ResponseEntity<Map<String, Object>> estado() {
        try {
            String version = jdbc.sql("SELECT VERSION()").query(String.class).single();

            Map<String, Object> jvm = new LinkedHashMap<>();
            jvm.put("totalMemory", Runtime.getRuntime().totalMemory());
            jvm.put("freeMemory", Runtime.getRuntime().freeMemory());
            jvm.put("maxMemory", Runtime.getRuntime().maxMemory());

            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("alumno", config.getAlumno());
            respuesta.put("prestamoMaximo", config.getPrestamoMaximo());
            respuesta.put("prestamoDias", config.getPrestamoDias());
            respuesta.put("mysqlVersion", version);
            respuesta.put("jvm", jvm);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("alumno", config.getAlumno());
            error.put("error", "Base de datos no disponible");
            return ResponseEntity.status(503).body(error);
        }
    }
}
