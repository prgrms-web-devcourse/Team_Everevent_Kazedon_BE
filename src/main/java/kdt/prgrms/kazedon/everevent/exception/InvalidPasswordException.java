package kdt.prgrms.kazedon.everevent.exception;

import java.text.MessageFormat;

public class InvalidPasswordException extends RuntimeException {

  public InvalidPasswordException(ErrorMessage errorMessage, String email) {
    super(MessageFormat.format(errorMessage.getMessage(), email));
  }
}
