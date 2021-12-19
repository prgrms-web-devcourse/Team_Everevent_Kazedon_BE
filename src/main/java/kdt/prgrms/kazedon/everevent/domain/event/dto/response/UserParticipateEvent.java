package kdt.prgrms.kazedon.everevent.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;

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

  @JsonProperty("eventId")
  public Long getEventId() {
    return eventId;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("expiredAt")
  public LocalDateTime getExpiredAt() {
    return expiredAt;
  }

  @JsonProperty("marketName")
  public String getMarketName() {
    return marketName;
  }

  @JsonProperty("likeCount")
  public int getLikeCount() {
    return likeCount;
  }

  @JsonProperty("reviewCount")
  public int getReviewCount() {
    return reviewCount;
  }

  @JsonProperty("isLike")
  public boolean isLike() {
    return isLike;
  }

  @JsonProperty("isCompleted")
  public boolean isCompleted() {
    return isCompleted;
  }

}
