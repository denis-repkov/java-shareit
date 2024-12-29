package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControllerExceptionHandlerTest {

    private final ControllerExceptionHandler handler = new ControllerExceptionHandler();

    @Test
    public void testHandleNotFoundException() {
        NotFoundException ex = new NotFoundException("Item not found");
        ResponseEntity<Map<String, String>> response = handler.handleNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody().get("error"));
        assertEquals("Item not found", response.getBody().get("message"));
    }

    @Test
    public void testHandleArgumentException() {
        MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
        ResponseEntity<Map<String, String>> response = handler.handleArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Wrong argument(s)", response.getBody().get("error"));
        assertEquals(ex.getMessage(), response.getBody().get("message"));
    }

    @Test
    public void testHandleAuthentificationException() {
        AuthentificationException ex = new AuthentificationException("Invalid credentials");
        ResponseEntity<Map<String, String>> response = handler.handleArgumentException(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Authentification error", response.getBody().get("error"));
        assertEquals("Invalid credentials", response.getBody().get("message"));
    }

    @Test
    public void testHandleGeneralException() {
        Exception ex = new Exception("Unexpected error occurred");
        ResponseEntity<Map<String, String>> response = handler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error", response.getBody().get("error"));
        assertEquals("Unexpected error occurred", response.getBody().get("message"));
    }
}
