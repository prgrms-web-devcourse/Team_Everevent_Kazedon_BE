package kdt.prgrms.kazedon.everevent.exception;

import java.text.MessageFormat;

public class InvalidDuplicationCheckTypeException extends RuntimeException{

    public InvalidDuplicationCheckTypeException(ErrorMessage errorMessage, String arg) {
        super(MessageFormat.format(errorMessage.getMessage(), arg));
    }
}
