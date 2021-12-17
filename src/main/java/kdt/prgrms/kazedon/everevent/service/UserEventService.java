package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.domain.userevent.UserEvent;
import kdt.prgrms.kazedon.everevent.domain.userevent.repository.UserEventRepository;
import kdt.prgrms.kazedon.everevent.exception.AlreadyParticipateException;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserEventService {

  private final EventRepository eventRepository;
  private final UserRepository userRepository;
  private final UserEventRepository userEventRepository;

  @Transactional
  public void participateEventByUser(Long userId, Long eventId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, userId));

    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, eventId));

    if (userEventRepository.existsUserEventsByUserIdAndEventId(userId, eventId)) {
      throw new AlreadyParticipateException(ErrorMessage.DUPLICATE_PARTICIPATE_EVENT, eventId);
    }

    UserEvent userEvent = UserEvent.builder()
            .user(user)
            .event(event)
            .build();

    userEvent.participateByUser();

    userEventRepository.save(userEvent);

    event.plusParticipantCount();
  }

  @Transactional
  public void completeEventByBusiness(Long userId, Long eventId) {
    eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, eventId));

    UserEvent userEvent = userEventRepository.findByUserIdAndEventId(userId, eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.PARTICIPATED_NOT_FOUNDED, eventId));

    if (userEvent.isCompleted()) {
      throw new AlreadyParticipateException(ErrorMessage.DUPLICATE_COMPLETED_EVENT, eventId);
    }

    userEvent.completeByBusiness();
  }
}
