package kdt.prgrms.kazedon.everevent.domain.event.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailEvent {

    private String eventName;
    private LocalDateTime expriedAt;
    private String marketName;
    private String marketDescription;
    private String eventDescription;

    private boolean like;
    private String completed;
    private boolean favorite;
}