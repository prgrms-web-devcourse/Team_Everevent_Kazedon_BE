package kdt.prgrms.kazedon.everevent.domain.event.dto;

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

  @JsonProperty(value = "isLike")
  private boolean like;

  @JsonProperty(value = "isParticipated")
  private boolean participated;
}
