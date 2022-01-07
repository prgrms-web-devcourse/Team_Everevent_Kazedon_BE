package kdt.prgrms.kazedon.everevent.service.converter;

import java.util.Collections;
import java.util.List;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.review.Review;
import kdt.prgrms.kazedon.everevent.domain.review.dto.request.ReviewWriteRequest;
import kdt.prgrms.kazedon.everevent.domain.review.dto.response.SimpleReview;
import kdt.prgrms.kazedon.everevent.domain.review.dto.response.SimpleReviewReadResponse;
import kdt.prgrms.kazedon.everevent.domain.review.dto.response.UserReview;
import kdt.prgrms.kazedon.everevent.domain.review.dto.response.UserReviewReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter {

  public Review convertToReview(
      User user,
      Event event,
      ReviewWriteRequest request,
      String pictureUrl
  ) {
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
        .pictureUrls(convertToPictureUrls(review.getPictureUrl()))
        .memberId(review.getUser().getId())
        .memberNickname(review.getUser().getNickname())
        .createdAt(review.getCreatedAt())
        .build();
  }

  public SimpleReviewReadResponse convertToSimpleReviewReadResponse(
      Page<SimpleReview> simpleReviews) {
    return SimpleReviewReadResponse.builder()
        .reviews(simpleReviews)
        .build();
  }

  public UserReviewReadResponse convertToUserReviewReadResponse(
      Page<UserReview> userReviews,
      long eventCount,
      long reviewCount
  ) {
    return UserReviewReadResponse.builder()
        .reviews(userReviews)
        .reviewerEventCount(eventCount)
        .reviewerReviewCount(reviewCount)
        .build();
  }

  private List<String> convertToPictureUrls(String pictureUrl) {
    return isBlankPictureUrl(pictureUrl) ? Collections.emptyList() : List.of(pictureUrl);
  }

  boolean isBlankPictureUrl(String pictureUrl) {
    return (pictureUrl == null) || (pictureUrl.isBlank());
  }

}
