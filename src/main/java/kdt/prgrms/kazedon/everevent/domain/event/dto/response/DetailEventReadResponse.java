package kdt.prgrms.kazedon.everevent.domain.event.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class DetailEventReadResponse {

    private String name;
    private LocalDateTime expriedAt;
    private String marketName;
    private String marketDescription;
    private String eventDescription;
    private List<String> pictures;
    private String participateStatus;
    private boolean isLike;
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

    @JsonProperty("pictures")
    public List<String> getPictures() {
        return pictures;
    }

    @JsonProperty("participateStatus")
    public String getParticipateStatus() {
        return participateStatus;
    }

    @JsonProperty("isLike")
    public boolean isLike() {
        return isLike;
    }

    @JsonProperty("isFavorite")
    public boolean isFavorite() {
        return isFavorite;
    }
}
