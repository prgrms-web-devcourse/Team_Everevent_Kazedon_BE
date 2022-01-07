package kdt.prgrms.kazedon.everevent.controller;

import javax.validation.Valid;
import kdt.prgrms.kazedon.everevent.configures.auth.AuthUser;
import kdt.prgrms.kazedon.everevent.domain.review.dto.response.SimpleReviewReadResponse;
import kdt.prgrms.kazedon.everevent.domain.review.dto.request.ReviewWriteRequest;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping(path = "/events/{eventId}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> writeReview(
      @AuthUser User user,
      @PathVariable Long eventId,
      @Valid @RequestPart ReviewWriteRequest request,
      @RequestPart(required = false) MultipartFile file
  ) {
    reviewService.createReview(user, eventId, request, file);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/events/{eventId}/reviews")
  public ResponseEntity<SimpleReviewReadResponse> getReviews(@PathVariable Long eventId,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(reviewService.getUserReviews(eventId, pageable));
  }

}
