package kdt.prgrms.kazedon.everevent.exception;

import java.text.MessageFormat;

public class DuplicateUserArgumentException extends RuntimeException {

  public DuplicateUserArgumentException(ErrorMessage errorMessage, String arg) {
    super(MessageFormat.format(errorMessage.getMessage(), arg));
  }
}
