package kdt.prgrms.kazedon.everevent.exception;

import java.text.MessageFormat;

public class AlreadyCreatedMarketException extends RuntimeException {

  public AlreadyCreatedMarketException(ErrorMessage errorMessage, Long userId) {
    super(MessageFormat.format(errorMessage.getMessage(), userId));
  }
}