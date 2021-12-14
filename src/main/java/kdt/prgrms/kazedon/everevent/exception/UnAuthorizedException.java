package kdt.prgrms.kazedon.everevent.exception;

import java.text.MessageFormat;

public class UnAuthorizedException extends RuntimeException {

  public UnAuthorizedException(ErrorMessage errorMessage, Long userId) {
    super(MessageFormat.format(errorMessage.getMessage(), userId));
  }
}
