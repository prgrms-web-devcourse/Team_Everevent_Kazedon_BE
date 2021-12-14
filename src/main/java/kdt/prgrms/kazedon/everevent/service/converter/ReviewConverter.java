package kdt.prgrms.kazedon.everevent.service.converter;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.review.Review;
import kdt.prgrms.kazedon.everevent.domain.review.dto.ReviewWriteRequest;
import kdt.prgrms.kazedon.everevent.domain.review.dto.SimpleReview;
import kdt.prgrms.kazedon.everevent.domain.review.dto.SimpleReviewReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.global.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter {

  private final S3Service s3Service;

  public ReviewConverter(S3Service s3Service) {
    this.s3Service = s3Service;
  }

  public Review convertToReview(User user, Event event, ReviewWriteRequest request, String pictureUrl) {
    return Review.builder()
        .user(user)
        .event(event)
        .description(request.getDescription())
        .pictureUrl(pictureUrl)
        .build();
  }

  public SimpleReview convertToSimpleReview(Review review) {
    return SimpleReview.builder()
        .reviewId(review.getId())
        .description(review.getDescription())
        .pictureUrl(s3Service.getFileUrl(review.getPictureUrl()))
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
