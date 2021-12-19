package kdt.prgrms.kazedon.everevent.domain.event.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailEvent {

    private String name;
    private LocalDateTime expriedAt;
    private String marketName;
    private String marketDescription;
    private String eventDescription;
    private boolean isLike;
    private String participateStatus;
    private boolean isFavorite;

    @JsonProperty("isLike")
    public boolean isLike() {
        return isLike;
    }

    @JsonProperty("isFavorite")
    public boolean isFavorite() {
        return isFavorite;
    }
}
