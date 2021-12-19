package kdt.prgrms.kazedon.everevent.domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class UserReviewReadResponse {
    private Page<UserReview> reviews;
    private long reviewerEventCount;
    private long reviewerReviewCount;
}
