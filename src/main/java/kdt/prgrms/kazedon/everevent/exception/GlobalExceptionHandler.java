package kdt.prgrms.kazedon.everevent.exception;

import kdt.prgrms.kazedon.everevent.exception.review.InvalidReviewArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<String> handleInvalidReviewArgument(InvalidReviewArgumentException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException exception){
    log.error(exception.getMessage());
    return ResponseEntity.badRequest().body(ErrorMessage.ARGUMENT_INVALID.getMessage());
  }

}
