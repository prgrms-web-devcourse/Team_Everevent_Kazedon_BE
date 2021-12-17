package kdt.prgrms.kazedon.everevent.domain.like.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SimpleEventLike {

  private Long eventId;
  private LocalDateTime expiredAt;
  private String name;
  private String marketName;
  private int likeCount;
  private int reviewCount;

}
