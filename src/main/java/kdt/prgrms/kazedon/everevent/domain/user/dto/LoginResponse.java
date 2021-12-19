package kdt.prgrms.kazedon.everevent.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

  private Long userId;
  private String nickname;
}
