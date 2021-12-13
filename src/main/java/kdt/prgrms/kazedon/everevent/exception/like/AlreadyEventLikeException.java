package kdt.prgrms.kazedon.everevent.exception.like;

import java.text.MessageFormat;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;

public class AlreadyEventLikeException extends RuntimeException {

  public AlreadyEventLikeException(ErrorMessage errorMessage, Long userId) {
    super(MessageFormat.format(errorMessage.getMessage(), userId));
  }

}
