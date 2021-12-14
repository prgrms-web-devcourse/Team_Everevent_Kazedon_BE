package kdt.prgrms.kazedon.everevent.exception;

public class InvalidFileTypeException extends RuntimeException{

  public InvalidFileTypeException(ErrorMessage errorMessage) {
    super(errorMessage.getMessage());
  }
}