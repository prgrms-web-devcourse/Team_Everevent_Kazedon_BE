package kdt.prgrms.kazedon.everevent.exception;

import java.text.MessageFormat;

public class AlreadyFavoritedException extends
RuntimeException{
  public AlreadyFavoritedException(ErrorMessage errorMessage, Long userId) {
    super(MessageFormat.format(errorMessage.getMessage(), userId));
  }
}
