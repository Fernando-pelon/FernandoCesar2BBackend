package FINALBOSS.CesarBackend.response;

import lombok.Getter;

// Clase común para todas las respuestas de la API
@Getter
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    // Constructor para respuestas que SÍ contienen datos
    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Constructor para respuestas que NO contienen datos (mensajes y errores)
    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
