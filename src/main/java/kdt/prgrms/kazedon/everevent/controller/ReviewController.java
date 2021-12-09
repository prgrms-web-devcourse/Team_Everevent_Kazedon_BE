package kdt.prgrms.kazedon.everevent.controller;

import kdt.prgrms.kazedon.everevent.configures.auth.AuthUser;
import kdt.prgrms.kazedon.everevent.configures.auth.CustomUserDetails;
import kdt.prgrms.kazedon.everevent.domain.review.Review;
import kdt.prgrms.kazedon.everevent.domain.review.dto.ReviewResponse;
import kdt.prgrms.kazedon.everevent.domain.review.dto.WriteReviewRequest;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping("/events/{eventId}/reviews")
  public ResponseEntity<ReviewResponse> writeReview(@AuthUser User user, @PathVariable Long eventId,
      @RequestBody WriteReviewRequest request){
    ReviewResponse reviewResponse = reviewService.createReview(user, eventId, request);
    return ResponseEntity.ok(reviewResponse);
  }

}
