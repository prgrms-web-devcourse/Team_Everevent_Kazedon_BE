package kdt.prgrms.kazedon.everevent.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

  private Long userId;
  private String nickname;

}
