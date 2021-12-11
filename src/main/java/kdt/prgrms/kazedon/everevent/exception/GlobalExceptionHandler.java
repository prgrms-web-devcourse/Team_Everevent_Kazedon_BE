package kdt.prgrms.kazedon.everevent.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<String> handleNotFound(NotFoundException exception) {
        log.warn(exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException exception){
        log.warn(exception.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
