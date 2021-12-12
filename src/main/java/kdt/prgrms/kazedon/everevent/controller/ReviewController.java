package kdt.prgrms.kazedon.everevent.controller;

import javax.validation.Valid;
import kdt.prgrms.kazedon.everevent.configures.auth.AuthUser;
import kdt.prgrms.kazedon.everevent.domain.review.dto.SimpleReviewReadResponse;
import kdt.prgrms.kazedon.everevent.domain.review.dto.ReviewResponse;
import kdt.prgrms.kazedon.everevent.domain.review.dto.ReviewWriteRequest;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping("/events/{eventId}/reviews")
  public ResponseEntity<ReviewResponse> writeReview(@AuthUser User user,
                                                    @PathVariable Long eventId,
                                                    @Valid @RequestBody ReviewWriteRequest request){
    ReviewResponse reviewResponse = reviewService.createReview(user, eventId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponse);
  }

  @GetMapping("/events/{eventId}/reviews")
  public ResponseEntity<SimpleReviewReadResponse> getReviews(@PathVariable Long eventId,
                                                       @PageableDefault(size=20, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable){
    return ResponseEntity.ok(reviewService.getReviews(eventId, pageable));
  }

}
