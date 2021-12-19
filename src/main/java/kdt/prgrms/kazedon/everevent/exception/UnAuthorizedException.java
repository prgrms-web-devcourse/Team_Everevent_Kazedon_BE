package kdt.prgrms.kazedon.everevent.exception;

import java.text.MessageFormat;

public class UnAuthorizedException extends RuntimeException {

  public UnAuthorizedException(ErrorMessage errorMessage, Long userId) {
    super(MessageFormat.format(errorMessage.getMessage(), userId));
  }

  public UnAuthorizedException(ErrorMessage errorMessage, String string) {
    super(MessageFormat.format(errorMessage.getMessage(), string));
  }
}
