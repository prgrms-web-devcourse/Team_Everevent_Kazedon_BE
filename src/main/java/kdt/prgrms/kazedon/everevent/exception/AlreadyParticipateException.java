package kdt.prgrms.kazedon.everevent.exception;

import java.text.MessageFormat;

public class AlreadyParticipateException extends
    RuntimeException {

  public AlreadyParticipateException(ErrorMessage errorMessage, Long eventId) {
    super(MessageFormat.format(errorMessage.getMessage(), eventId));
  }
}
