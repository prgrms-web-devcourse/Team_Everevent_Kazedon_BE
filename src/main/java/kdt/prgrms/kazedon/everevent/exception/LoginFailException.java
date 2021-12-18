package kdt.prgrms.kazedon.everevent.exception;

import java.text.MessageFormat;

public class LoginFailException extends RuntimeException {

  public LoginFailException(ErrorMessage errorMessage, String email) {
    super(MessageFormat.format(errorMessage.getMessage(), email));
  }
}
