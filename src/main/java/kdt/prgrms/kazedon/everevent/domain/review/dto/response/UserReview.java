package kdt.prgrms.kazedon.everevent.domain.review.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class UserReview {

    private Long reviewId;
    private String description;
    private String marketName;
    private String pictureUrl;

    @JsonProperty("reviewId")
    public Long getReviewId() {
        return reviewId;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("marketName")
    public String getMarketName() {
        return marketName;
    }

    @JsonProperty("pictureUrl")
    public String getPictureUrl() {
        return pictureUrl;
    }

}
