package kdt.prgrms.kazedon.everevent.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class SimpleEvent {

    private Long eventId;
    private String name;
    private LocalDateTime expiredAt;
    private String marketName;
    private String pictureUrl;
    private int likeCount;
    private int reviewCount;
    private boolean isLike;
    private int remainingParticipants;

    @JsonProperty("eventId")
    public Long getEventId() {
        return eventId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("expiredAt")
    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    @JsonProperty("marketName")
    public String getMarketName() {
        return marketName;
    }

    @JsonProperty("picturleUrl")
    public String getPictureUrl() {
        return pictureUrl;
    }

    @JsonProperty("likeCount")
    public int getLikeCount() {
        return likeCount;
    }

    @JsonProperty("reviewCount")
    public int getReviewCount() {
        return reviewCount;
    }

    @JsonProperty("isLike")
    public boolean isLike() {
        return isLike;
    }

    @JsonProperty("remainingParticipants")
    public int getRemainingParticipants() {
        return remainingParticipants;
    }

}
