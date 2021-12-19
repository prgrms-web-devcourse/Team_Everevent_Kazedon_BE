package kdt.prgrms.kazedon.everevent.domain.market.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class MyMarketReadResponse {

    private long marketId;
    private String name;
    private String description;
    private int eventCount;
    private int likeCount;
    private int reviewCount;

    @JsonProperty("marketId")
    public long getMarketId() {
        return marketId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("eventCount")
    public int getEventCount() {
        return eventCount;
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
