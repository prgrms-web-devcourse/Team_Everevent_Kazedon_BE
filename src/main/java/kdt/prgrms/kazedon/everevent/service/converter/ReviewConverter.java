package kdt.prgrms.kazedon.everevent.service.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.review.Review;
import kdt.prgrms.kazedon.everevent.domain.review.dto.ReviewWriteRequest;
import kdt.prgrms.kazedon.everevent.domain.review.dto.SimpleReview;
import kdt.prgrms.kazedon.everevent.domain.review.dto.SimpleReviewReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter {

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
        .pictureUrls(convertToPictureUrls(review.getPictureUrl()))
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

  private List<String> convertToPictureUrls(String pictureUrl) {
    return new ArrayList<>(List.of(pictureUrl));
  }

}
