package kdt.prgrms.kazedon.everevent.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SimpleEvent {
    private Long eventId;
    private String eventName;
    private LocalDateTime expiredAt;
    private String marketName;
    private int likeCount;
    private int reviewCount;
    private boolean isLike;
    private int remainingParticipants;
}
