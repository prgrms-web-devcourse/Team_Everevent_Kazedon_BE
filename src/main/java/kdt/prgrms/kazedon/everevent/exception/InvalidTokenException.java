package kdt.prgrms.kazedon.everevent.exception;

import java.text.MessageFormat;

public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException(ErrorMessage errorMessage, String token) {
    super(MessageFormat.format(errorMessage.getMessage(), token));
  }
}
