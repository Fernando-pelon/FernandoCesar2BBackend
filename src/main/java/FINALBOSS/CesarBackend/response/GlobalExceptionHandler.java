package FINALBOSS.CesarBackend.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

// Captura los errores de toda la API y los responde con ApiResponse
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Falla alguna validación del DTO (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        if (mensaje.isEmpty()) {
            mensaje = "Los datos enviados no son válidos";
        }
        log.warn("Validación fallida: {}", mensaje);
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, mensaje));
    }

    // El JSON viene mal escrito o con un dato en formato incorrecto (por ejemplo la fecha)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        log.warn("JSON inválido: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(400, "El JSON enviado no es válido. Revise los tipos de dato y que la fecha sea AAAA-MM-DD"));
    }

    // El id de la URL no es un número
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> manejarTipoIncorrecto(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(400, "El valor enviado en la URL no tiene el formato correcto"));
    }

    // Errores de reglas de negocio y registros no encontrados (los lanzan los services)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Object>> manejarReglaDeNegocio(ResponseStatusException ex) {
        int status = ex.getStatusCode().value();
        log.warn("Regla de negocio: {}", ex.getReason());
        return ResponseEntity.status(status).body(new ApiResponse<>(status, ex.getReason()));
    }

    // Errores de la base de datos: llave foránea, valor repetido, etc.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> manejarIntegridad(DataIntegrityViolationException ex) {
        log.warn("Violación de integridad: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(400, "No se pudo completar la operación: hay un valor repetido o el registro está relacionado con otros datos"));
    }

    // Cualquier otro error inesperado
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> manejarErrorGeneral(RuntimeException ex) {
        log.error("Error interno del servidor", ex);
        return ResponseEntity.status(500).body(new ApiResponse<>(500, "Error interno del servidor"));
    }
}
