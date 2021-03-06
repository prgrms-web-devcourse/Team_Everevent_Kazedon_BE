package kdt.prgrms.kazedon.everevent.exception;

public enum ErrorMessage {
  EVENT_NOT_FOUNDED("아이디 {0}에 해당하는 이벤트 정보가 없습니다."),
  USER_NOT_FOUNDED("이메일 {0} 사용자 정보가 없습니다."),
  USER_ID_NOT_FOUNDED("아이디 {0} 사용자 정보가 없습니다."),
  MARKET_NOT_FOUNDED("아이디 {0}에 해당하는 가게 정보가 없습니다."),
  DUPLICATE_FAVORITE_MARKET("아이디 {0}은 이미 존재하는 즐겨찾기 항목입니다."),
  DUPLICATE_NOT_FAVORITE_MARKET("아이디 {0}은 이미 삭제한 즐겨찾기 항목입니다."),
  INVALID_EMAIL_FORMAT("이메일 {0}은 잘못된 형식입니다."),
  DUPLICATE_NICKNAME_ARGUMENT("닉네임 {0}은 이미 존재하는 닉네임입니다."),
  DUPLICATE_EMAIL_ARGUMENT("이메일 {0}은 이미 존재하는 이메일입니다."),
  FAVORITE_NOT_FOUNDED("즐겨찾기 {0}은 존재하지 않는 항목입니다."),
  DUPLICATE_EVENT_LIKE("아이디 {0}은 이미 존재하는 좋아요 항목입니다."),
  EVENTLIKE_NOT_FOUNDED("이미 삭제된 좋아요 항목입니다."),
  REVIEW_ARGUMENT_INVALID("댓글 {0} 인자의 범위가 옳지 않습니다."),
  ARGUMENT_INVALID("인자가 유효하지 않습니다."),
  DUPLICATE_PARTICIPATE_EVENT("이벤트 {0}은 이미 참여한 항목입니다."),
  DUPLICATE_COMPLETED_EVENT("이벤트 {0}은 이미 완료한 항목입니다."),
  UNAUTHORIZED_USER("사용자 {0}는 접근 권한이 없습니다."),
  PARTICIPATED_NOT_FOUNDED("이벤트 {0} 참여 내역 정보가 없습니다."),
  FILE_UPLOAD_ERROR("파일 업로드 중 에러가 발생하였습니다"),
  INVALID_FILE_TYPE("잘못된 파일 타입입니다."),
  ALREADY_REGISTER_MARKET("이미 가게를 등록했습니다."),
  LOGIN_FAILED("이메일 {0}은 로그인에 실패했습니다."),
  INVALID_PASSWORD("이메일 {0}의 기존 비밀번호와 일치하지 않습니다."),
  INVALID_TOKEN("{0}는 만료 또는 잘못된 토큰입니다."),
  UNAUTHORIZED_CREATE_EVENT("사용자 {0}은 이벤트를 만들 권한이 없습니다"),
  USER_NOT_PARTICIPATED_EVENT("사용자 {0}는 이벤트 {1}에 참여하지 않았습니다."),
  UNAUTHORIZED_UPDATE_MARKET("사용자 {0}은 가게를 수정할 권한이 없습니다"),
  INVALID_DUPLICATION_CHECK_TYPE("사용자의 {0} 중복 체크는 지원하지 않습니다.");

  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
