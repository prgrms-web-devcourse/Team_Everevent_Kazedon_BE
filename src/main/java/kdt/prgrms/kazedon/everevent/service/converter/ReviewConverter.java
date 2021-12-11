package kdt.prgrms.kazedon.everevent.service.converter;

import java.time.LocalDateTime;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.review.Review;
import kdt.prgrms.kazedon.everevent.domain.review.dto.ReviewResponse;
import kdt.prgrms.kazedon.everevent.domain.review.dto.ReviewWriteRequest;
import kdt.prgrms.kazedon.everevent.domain.review.dto.SimpleReview;
import kdt.prgrms.kazedon.everevent.domain.review.dto.SimpleReviewReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter {

  public Review convertToReview(User user, Event event, ReviewWriteRequest request) {
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

  public SimpleReview convertToSimpleEvent(Review review) {
    return SimpleReview.builder()
        .reviewId(review.getId())
        .description(review.getDescription())
        .pictureUrl(review.getPictureUrl())
        .memberId(review.getUser().getId())
        .memberNickname(review.getUser().getNickname())
        .createdAt(review.getCreatedAt())
        .build();
  }

  public SimpleReviewReadResponse convertToSimpleReviewReadResponse(Page<SimpleReview> simpleReviews) {
    return SimpleReviewReadResponse.builder()
        .simpleReviews(simpleReviews)
        .build();
  }
}
