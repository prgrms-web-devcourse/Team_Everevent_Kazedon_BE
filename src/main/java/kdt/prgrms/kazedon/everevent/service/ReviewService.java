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
import kdt.prgrms.kazedon.everevent.service.global.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;

  private final EventRepository eventRepository;

  private final FileService fileService;

  private final ReviewConverter reviewConverter;

  @Transactional
  public void createReview(User user, Long eventId, ReviewWriteRequest request, MultipartFile file) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, eventId));

    String pictureUrl = fileService.uploadImage(file);

    Review review = reviewConverter.convertToReview(user, event, request, pictureUrl);

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
