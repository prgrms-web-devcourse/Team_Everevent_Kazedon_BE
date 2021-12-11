package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.review.Review;
import kdt.prgrms.kazedon.everevent.domain.review.dto.ReviewResponse;
import kdt.prgrms.kazedon.everevent.domain.review.dto.ReviewWriteRequest;
import kdt.prgrms.kazedon.everevent.domain.review.repository.ReviewRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.converter.ReviewConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;

  private final EventRepository eventRepository;

  private final ReviewConverter reviewConverter;

  public ReviewResponse createReview(User user, Long eventId, ReviewWriteRequest request) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(
            () -> new RuntimeException(String.format("user(id : %d) not founded", eventId)));

    Review review = reviewConverter.convertToReview(user, event, request);
    reviewRepository.save(review);

    return reviewConverter.convertToReviewResponse(review);
  }

}
