package kdt.prgrms.kazedon.everevent.exception;

import kdt.prgrms.kazedon.everevent.exception.like.AlreadyEventLikeException;
import kdt.prgrms.kazedon.everevent.exception.review.InvalidReviewArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<String> handleInvalidReviewArgument(
      InvalidReviewArgumentException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.badRequest().body(ErrorMessage.ARGUMENT_INVALID.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleNotFound(NotFoundException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleAlreadyEventLike(AlreadyEventLikeException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleAlreadyFavorite(AlreadyFavoritedException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleInvalidUserArgument(
      InvalidUserArgumentException exception) {
    log.warn(exception.getMessage());
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleFileUploadError(
      FileUploadException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleInvalidFileType(
      InvalidFileTypeException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleAlreadyParticipate(AlreadyParticipateException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleUnAuthorized(UnAuthorizedException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleLoginFailed(LoginFailException exception) {
    log.warn(exception.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
  }

}
