package kdt.prgrms.kazedon.everevent.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class LoginResponse {

  private Long userId;
  private String nickname;

  @JsonProperty("userId")
  public Long getUserId() {
    return userId;
  }

  @JsonProperty("nickname")
  public String getNickname() {
    return nickname;
  }

}
