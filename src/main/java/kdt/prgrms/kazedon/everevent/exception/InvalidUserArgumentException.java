package kdt.prgrms.kazedon.everevent.exception;

import java.text.MessageFormat;

public class InvalidUserArgumentException extends RuntimeException {

  public InvalidUserArgumentException(ErrorMessage errorMessage, String email) {
    super(MessageFormat.format(errorMessage.getMessage(), email));
  }
}
