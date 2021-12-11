package kdt.prgrms.kazedon.everevent.domain.review.dto;

import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEvent;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class SimpleReviewReadResponse {
  private Page<SimpleReview> simpleReviews;
}
