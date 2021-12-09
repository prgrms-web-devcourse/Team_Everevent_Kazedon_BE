package kdt.prgrms.kazedon.everevent.exception;

public enum ErrorMessage {
    EVENT_NOT_FOUNDED("아이디 {0}에 해당하는 이벤트 정보가 없습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
