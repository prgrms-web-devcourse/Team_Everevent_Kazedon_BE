package kdt.prgrms.kazedon.everevent.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

  private String email;

  private String password;

  private String nickname;

  public void encodingPassword(String encode) {
    password = encode;
  }
}
