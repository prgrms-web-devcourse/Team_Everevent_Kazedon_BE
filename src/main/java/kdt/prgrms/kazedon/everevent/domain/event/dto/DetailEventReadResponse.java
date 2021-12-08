package kdt.prgrms.kazedon.everevent.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class DetailEventReadResponse {
    private Long eventId;
    private String eventName;
    private LocalDateTime expriedAt;
    private String marketName;
    private String marketDescription;
    private String eventDescription;
    private boolean isLike;
    private boolean isFavorite;
    private boolean isParticipated;
    private List<String> pictures;
}
