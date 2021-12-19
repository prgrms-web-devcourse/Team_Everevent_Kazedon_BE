package kdt.prgrms.kazedon.everevent.domain.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class SimpleReviewReadResponse {

  private Page<SimpleReview> reviews;

}
