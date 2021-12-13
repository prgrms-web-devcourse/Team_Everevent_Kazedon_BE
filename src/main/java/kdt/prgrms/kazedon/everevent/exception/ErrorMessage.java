package kdt.prgrms.kazedon.everevent.exception;

public enum ErrorMessage {
  EVENT_NOT_FOUNDED("아이디 {0}에 해당하는 이벤트 정보가 없습니다."),
  USER_NOT_FOUNDED("이메일 {0} 사용자 정보가 없습니다."),
  DUPLICATE_EVENT_LIKE("아이디 {0}은 이미 존재하는 좋아요 항목입니다."),
  EVENTLIKE_NOT_FOUNDED("이미 삭제된 좋아요 항목입니다."),
  REVIEW_ARGUMENT_INVALID("댓글 {0} 인자의 범위가 옳지 않습니다."),
  ARGUMENT_INVALID("인자가 유효하지 않습니다."),
  MARKET_NOT_FOUNDED("아이디 {0}에 해당하는 가게 정보가 없습니다");

  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}