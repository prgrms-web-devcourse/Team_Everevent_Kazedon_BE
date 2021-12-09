package kdt.prgrms.kazedon.everevent.domain.user.dto;

import kdt.prgrms.kazedon.everevent.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SimpleUserResponse {

  private String email;

  private String password;

  private String nickname;

  private String location;

  public SimpleUserResponse(User user) {
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.nickname = user.getNickname();
    this.location = user.getLocation();
  }

}
