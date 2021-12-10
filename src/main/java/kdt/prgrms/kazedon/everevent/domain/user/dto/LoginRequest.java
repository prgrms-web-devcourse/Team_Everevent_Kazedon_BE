package kdt.prgrms.kazedon.everevent.domain.user.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

  private String email;

  private String password;

  public void encodingPassword(String encode) {
    password = encode;
  }
}
