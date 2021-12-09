package kdt.prgrms.kazedon.everevent.exception;

import java.text.MessageFormat;

public class NotFoundException extends RuntimeException {
    public NotFoundException(ErrorMessage errorMessage, Long id) {
        super(
                MessageFormat.format(errorMessage.getMessage(), id)
        );
    }
}