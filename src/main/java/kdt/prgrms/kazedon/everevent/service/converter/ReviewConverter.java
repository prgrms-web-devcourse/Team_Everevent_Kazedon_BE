package kdt.prgrms.kazedon.everevent.service.converter;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.review.Review;
import kdt.prgrms.kazedon.everevent.domain.review.dto.ReviewResponse;
import kdt.prgrms.kazedon.everevent.domain.review.dto.WriteReviewRequest;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter {

  public Review convertToReview(User user, Event event,
      WriteReviewRequest request) {
    return Review.builder()
        .user(user)
        .event(event)
        .description(request.getDescription())
        .build();

  }

  public ReviewResponse convertToReviewResponse(Review review) {
    return ReviewResponse.builder()
        .reviewId(review.getId())
        .build();
  }

}
