package kdt.prgrms.kazedon.everevent.domain.review.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public class UserReviewReadResponse {

    private Page<UserReview> reviews;
    private long reviewerEventCount;
    private long reviewerReviewCount;

    @JsonProperty("reviews")
    public Page<UserReview> getReviews() {
        return reviews;
    }

    @JsonProperty("reviewerEventCount")
    public long getReviewerEventCount() {
        return reviewerEventCount;
    }

    @JsonProperty("reviewerReviewCount")
    public long getReviewerReviewCount() {
        return reviewerReviewCount;
    }

}
