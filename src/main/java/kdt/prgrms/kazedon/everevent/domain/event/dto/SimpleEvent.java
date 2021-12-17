package kdt.prgrms.kazedon.everevent.domain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SimpleEvent {
    private Long eventId;
    private String eventName;
    private LocalDateTime expiredAt;
    private String marketName;
    private String pictureUrl;
    private int likeCount;
    private int reviewCount;
    private boolean isLike;
    private int remainingParticipants;
}
