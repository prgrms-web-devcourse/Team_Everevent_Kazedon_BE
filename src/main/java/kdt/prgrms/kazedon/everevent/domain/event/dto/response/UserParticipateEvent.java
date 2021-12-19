package kdt.prgrms.kazedon.everevent.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserParticipateEvent {

  private Long eventId;
  private String name;
  private LocalDateTime expiredAt;
  private String marketName;
  private int likeCount;
  private int reviewCount;
  private boolean isLike;
  private boolean isCompleted;

  @JsonProperty("isLike")
  public boolean isLike() {
    return isLike;
  }

  @JsonProperty("isCompleted")
  public boolean isCompleted() {
    return isCompleted;
  }

}
