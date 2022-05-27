package tech.zerofiltre.freeland.infra.entrypoints.rest;

import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.error.*;

import java.util.*;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<FreelandError> handleGenericProblem(Throwable throwable, Locale locale) {
        final FreelandError error = new FreelandError(
                Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "Une erreur est survenue, veuillez reéssayer plus tard",
                throwable.getLocalizedMessage()
        );
        log.debug("Trace complète", throwable);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
