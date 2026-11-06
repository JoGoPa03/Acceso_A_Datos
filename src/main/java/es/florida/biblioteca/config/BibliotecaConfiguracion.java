package es.florida.biblioteca.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BibliotecaConfiguracion {

    @Value("${biblioteca.alumno}")
    private String alumno;

    @Value("${biblioteca.prestamo.maximo}")
    private int prestamoMaximo;

    @Value("${biblioteca.prestamo.dias}")
    private int prestamoDias;

    public String getAlumno() { return alumno; }
    public int getPrestamoMaximo() { return prestamoMaximo; }
    public int getPrestamoDias() { return prestamoDias; }
}
