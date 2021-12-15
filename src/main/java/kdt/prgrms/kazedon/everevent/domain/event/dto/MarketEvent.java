package kdt.prgrms.kazedon.everevent.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MarketEvent {
    private Long eventId;
    private LocalDateTime expiredAt;
    private String eventName;
    private String marketName;
    private int likeCount;
    private int reviewCount;
}
