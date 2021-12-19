package kdt.prgrms.kazedon.everevent.domain.review.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public class SimpleReviewReadResponse {

  private Page<SimpleReview> reviews;

  @JsonProperty("reviews")
  public Page<SimpleReview> getReviews() {
    return reviews;
  }

}
