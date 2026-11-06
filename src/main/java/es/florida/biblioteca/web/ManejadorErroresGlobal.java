package es.florida.biblioteca.web;

import es.florida.biblioteca.excepcion.ConflictoException;
import es.florida.biblioteca.excepcion.ErrorRespuesta;
import es.florida.biblioteca.excepcion.PeticionIncorrectaException;
import es.florida.biblioteca.excepcion.RecursoNoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ManejadorErroresGlobal {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> noEncontrado(RecursoNoEncontradoException ex, HttpServletRequest req) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI(), List.of());
    }

    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ErrorRespuesta> conflicto(ConflictoException ex, HttpServletRequest req) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI(), List.of());
    }

    @ExceptionHandler(PeticionIncorrectaException.class)
    public ResponseEntity<ErrorRespuesta> peticionIncorrecta(PeticionIncorrectaException ex, HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        return respuesta(HttpStatus.BAD_REQUEST, "Error de validación", req.getRequestURI(), detalles);
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<ErrorRespuesta> baseDatosNoDisponible(DataAccessResourceFailureException ex, HttpServletRequest req) {
        return respuesta(HttpStatus.SERVICE_UNAVAILABLE, "Base de datos no disponible", req.getRequestURI(), List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> errorGeneral(Exception ex, HttpServletRequest req) {
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req.getRequestURI(), List.of());
    }

    private ResponseEntity<ErrorRespuesta> respuesta(HttpStatus estado, String mensaje, String ruta, List<String> detalles) {
        var error = new ErrorRespuesta(estado.value(), estado.getReasonPhrase(), mensaje, ruta, detalles);
        return ResponseEntity.status(estado).body(error);
    }
}
