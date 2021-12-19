package kdt.prgrms.kazedon.everevent.domain.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class UserReviewReadResponse {

    private Page<UserReview> reviews;
    private long reviewerEventCount;
    private long reviewerReviewCount;

}
