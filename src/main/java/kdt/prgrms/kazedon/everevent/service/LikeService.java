package kdt.prgrms.kazedon.everevent.service;


import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.like.EventLike;
import kdt.prgrms.kazedon.everevent.domain.like.dto.response.SimpleEventLike;
import kdt.prgrms.kazedon.everevent.domain.like.dto.response.SimpleEventLikeReadResponse;
import kdt.prgrms.kazedon.everevent.domain.like.repository.EventLikeRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.like.AlreadyEventLikeException;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.EventLikeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

  private final EventLikeRepository eventLikeRepository;

  private final UserRepository userRepository;

  private final EventRepository eventRepository;

  private final EventLikeConverter eventLikeConverter;

  @Transactional
  public void addLike(User user, Long eventId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, eventId));

    if (eventLikeRepository.existsEventLikeByUserIdAndEventId(user.getId(), eventId)) {
      throw new AlreadyEventLikeException(ErrorMessage.DUPLICATE_EVENT_LIKE, user.getId());
    }

    EventLike eventLike = eventLikeConverter.convertToEventLike(user, event);
    eventLikeRepository.save(eventLike);
    event.plusOneLike();

  }

  @Transactional
  public void deleteLike(User user, Long eventId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, eventId));

    EventLike eventLike = eventLikeRepository.findByUserIdAndEventId(user.getId(), eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENTLIKE_NOT_FOUNDED, eventId));

    eventLikeRepository.deleteById(eventLike.getId());
    event.minusOneLike();
  }

  @Transactional(readOnly = true)
  public SimpleEventLikeReadResponse getLikes(Long memberId, Pageable pageable) {
    Page<SimpleEventLike> simpleEventLikes = eventLikeRepository
        .findSimpleLikeByUserId(memberId, pageable);

    return eventLikeConverter.convertToSimpleEventLikeReadResponse(simpleEventLikes);
  }
}
