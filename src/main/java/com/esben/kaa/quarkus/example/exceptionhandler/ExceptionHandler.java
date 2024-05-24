package com.esben.kaa.quarkus.example.exceptionhandler;

import com.esben.kaa.quarkus.example.exception.ResourceNotFoundException;
import com.esben.kaa.quarkus.example.exception.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof ResourceNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponseBody(exception.getMessage()))
                    .build();
        }
        if (exception instanceof ValidationException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponseBody(exception.getMessage()))
                    .build();
        }
        log.error(exception.getMessage(), exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponseBody("Something unexpected happened. Try again"))
                .build();
    }

    public static final class ErrorResponseBody {

        private final String message;

        public ErrorResponseBody(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
