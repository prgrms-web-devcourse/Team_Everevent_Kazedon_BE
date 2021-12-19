package kdt.prgrms.kazedon.everevent.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class MarketEvent {

    private Long eventId;
    private LocalDateTime expiredAt;
    private String name;
    private String marketName;
    private int likeCount;
    private int reviewCount;

    @JsonProperty("eventId")
    public Long getEventId() {
        return eventId;
    }

    @JsonProperty("expiredAt")
    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
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
}
