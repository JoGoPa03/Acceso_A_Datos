package es.florida.biblioteca.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AlumnoFiltro extends OncePerRequestFilter {

    private final BibliotecaConfiguracion config;

    public AlumnoFiltro(BibliotecaConfiguracion config) {
        this.config = config;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        response.setHeader("X-Alumno", config.getAlumno());
        chain.doFilter(request, response);
    }
}
