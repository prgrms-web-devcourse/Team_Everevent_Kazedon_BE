package kdt.prgrms.kazedon.everevent.exception.review;

import java.text.MessageFormat;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;

public class InvalidReviewArgumentException extends RuntimeException {

  public InvalidReviewArgumentException(String arg){
    super(MessageFormat.format(ErrorMessage.REVIEW_ARGUMENT_INVALID.getMessage(), arg));
  }

}
