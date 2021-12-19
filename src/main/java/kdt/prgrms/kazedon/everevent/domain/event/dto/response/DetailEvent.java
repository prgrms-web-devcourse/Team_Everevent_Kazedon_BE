package kdt.prgrms.kazedon.everevent.domain.event.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

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

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("expiredAt")
    public LocalDateTime getExpriedAt() {
        return expriedAt;
    }

    @JsonProperty("marketName")
    public String getMarketName() {
        return marketName;
    }

    @JsonProperty("marketDescription")
    public String getMarketDescription() {
        return marketDescription;
    }

    @JsonProperty("eventDescription")
    public String getEventDescription() {
        return eventDescription;
    }

    @JsonProperty("isLike")
    public boolean isLike() {
        return isLike;
    }

    @JsonProperty("participateStatus")
    public String getParticipateStatus() {
        return participateStatus;
    }

    @JsonProperty("isFavorite")
    public boolean isFavorite() {
        return isFavorite;
    }
}
