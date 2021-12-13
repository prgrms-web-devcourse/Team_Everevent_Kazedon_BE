package kdt.prgrms.kazedon.everevent.service;


import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.like.EventLike;
import kdt.prgrms.kazedon.everevent.domain.like.repository.EventLikeRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.like.AlreadyEventLikeException;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

  private final EventLikeRepository eventLikeRepository;

  private final UserRepository userRepository;

  private final EventRepository eventRepository;

  @Transactional
  public void addLike(Long userId, Long eventId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, userId));

    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, eventId));

    if (eventLikeRepository.existsEventLikeByUserIdAndEventId(userId, eventId)) {
      throw new AlreadyEventLikeException(ErrorMessage.DUPLICATE_EVENT_LIKE, userId);
    }

    EventLike eventLike = EventLike.builder()
                                .user(user)
                                .event(event)
                                .build();
    eventLikeRepository.save(eventLike);
    event.plusOneLike();

  }

  @Transactional
  public void deleteLike(Long userId, Long eventId) {
    EventLike eventLike = eventLikeRepository.findByUserIdAndEventId(userId, eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENTLIKE_NOT_FOUNDED, eventId));

    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, eventId));

    if (!eventLikeRepository.existsEventLikeByUserIdAndEventId(userId, eventId)) {
      throw new AlreadyEventLikeException(ErrorMessage.DUPLICATE_EVENT_UNLIKE, userId);
    }

    eventLikeRepository.deleteById(eventLike.getId());
    event.minusOneLike();

  }

}