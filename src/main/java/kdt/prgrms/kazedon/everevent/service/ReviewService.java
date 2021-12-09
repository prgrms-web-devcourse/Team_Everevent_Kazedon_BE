package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.review.Review;
import kdt.prgrms.kazedon.everevent.domain.review.dto.ReviewResponse;
import kdt.prgrms.kazedon.everevent.domain.review.dto.WriteReviewRequest;
import kdt.prgrms.kazedon.everevent.domain.review.repository.ReviewRepository;
import kdt.prgrms.kazedon.everevent.service.converter.ReviewConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;

  private final EventRepository eventRepository;

  private final ReviewConverter reviewConverter;

  public ReviewResponse createReview(Long eventId, WriteReviewRequest request) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new RuntimeException(String.format("user(id : %d) not founded", eventId)));

    Review review = reviewConverter.convertToReview(event, request);
    reviewRepository.save(review);

    return reviewConverter.convertToReviewResponse(review);
  }

}
