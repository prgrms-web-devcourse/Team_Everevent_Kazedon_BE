package kdt.prgrms.kazedon.everevent.domain.user.dto;

import lombok.Getter;

@Getter
public class SignUpRequest {

  private String email;

  private String password;

  private String nickname;

  public void encodingPassword(String encode) {
    password = encode;
  }
}
