package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.review.Review;
import kdt.prgrms.kazedon.everevent.domain.review.dto.SimpleReviewReadResponse;
import kdt.prgrms.kazedon.everevent.domain.review.dto.ReviewWriteRequest;
import kdt.prgrms.kazedon.everevent.domain.review.dto.SimpleReview;
import kdt.prgrms.kazedon.everevent.domain.review.repository.ReviewRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.ReviewConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;

  private final EventRepository eventRepository;

  private final ReviewConverter reviewConverter;

  @Transactional
  public void createReview(User user, Long eventId, ReviewWriteRequest request) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, eventId));

    Review review = reviewConverter.convertToReview(user, event, request);
    reviewRepository.save(review);
  }

  @Transactional(readOnly = true)
  public SimpleReviewReadResponse getReviews(Long eventId, Pageable pageable) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, eventId));

    Page<SimpleReview> simpleReviews = reviewRepository
        .findByEvent(event, pageable)
        .map(reviewConverter::convertToSimpleReview);

    return reviewConverter.convertToSimpleReviewReadResponse(simpleReviews);
  }

}
