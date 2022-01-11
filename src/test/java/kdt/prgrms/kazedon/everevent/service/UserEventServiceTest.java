package kdt.prgrms.kazedon.everevent.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.userevent.UserEvent;
import kdt.prgrms.kazedon.everevent.domain.userevent.repository.UserEventRepository;
import kdt.prgrms.kazedon.everevent.exception.AlreadyParticipateException;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserEventServiceTest {

  @InjectMocks
  private UserEventService userEventService;

  @Mock
  private EventRepository eventRepository;

  @Mock
  private UserEventRepository userEventRepository;

  private final User user = User.builder()
      .email("test-email8@gmail.com")
      .password("test-password")
      .nickname("test-nickname")
      .location("test-location")
      .build();

  private final Market market = Market.builder()
      .user(user)
      .name("test-market-name")
      .description("test-description")
      .address("test-market-address")
      .build();

  private final Event event = Event.builder()
      .market(market)
      .name("test-event-name")
      .expiredAt(LocalDateTime.now())
      .description("test-description")
      .maxParticipants(5)
      .build();

  private final UserEvent userEvent = UserEvent.builder()
      .user(user)
      .event(event)
      .build();

  @Test
  void participateEventByUserSuccessTest() {
    //given
    userEvent.participateByUser();
    when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
    when(userEventRepository.existsUserEventsByUserIdAndEventId(user.getId(),
        event.getId())).thenReturn(false);
    when(userEventRepository.save(any())).thenReturn(userEvent);

    //when
    userEventService.participateEventByUser(user, event.getId());

    //then
    verify(eventRepository).findById(event.getId());
    verify(userEventRepository).existsUserEventsByUserIdAndEventId(user.getId(), event.getId());
    verify(userEventRepository).save(any());
  }

  @Test
  void participateEventNotExistTest() {
    //given
    userEvent.participateByUser();
    when(eventRepository.findById(2L)).thenReturn(Optional.empty());

    //when
    assertThrows(NotFoundException.class,
        () -> userEventService.participateEventByUser(user, 2L));

    //then
    verify(eventRepository).findById(2L);
  }

  @Test
  void participateEventDuplicateParticipateTest() {
    //given
    userEvent.participateByUser();
    when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
    when(userEventRepository.existsUserEventsByUserIdAndEventId(user.getId(),
        event.getId())).thenReturn(true);

    //when
    assertThrows(AlreadyParticipateException.class,
        () -> userEventService.participateEventByUser(user, event.getId()));

    //then
    verify(eventRepository).findById(event.getId());
    verify(userEventRepository).existsUserEventsByUserIdAndEventId(user.getId(), event.getId());
  }

  @Test
  void completeEventByBusinessSuccessTest() {
    //given
    userEvent.participateByUser();
    when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
    when(userEventRepository.findByUserIdAndEventId(user.getId(), event.getId())).thenReturn(
        Optional.of(userEvent));

    //when
    userEventService.completeEventByBusiness(user, event.getId());

    //then
    verify(eventRepository).findById(event.getId());
    verify(userEventRepository).findByUserIdAndEventId(user.getId(), event.getId());
  }


  @Test
  void completeEventNotExistTest() {
    //given
    userEvent.participateByUser();
    when(eventRepository.findById(2L)).thenReturn(Optional.empty());

    //when
    assertThrows(NotFoundException.class,
        () -> userEventService.completeEventByBusiness(user, 2L));

    //then
    verify(eventRepository).findById(2L);
  }

  @Test
  void completeEventNotParticipateTest() {
    //given
    userEvent.participateByUser();
    when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
    when(userEventRepository.findByUserIdAndEventId(user.getId(), event.getId())).thenReturn(
        Optional.empty());

    //when
    assertThrows(NotFoundException.class,
        () -> userEventService.completeEventByBusiness(user, event.getId()));

    //then
    verify(eventRepository).findById(event.getId());
    verify(userEventRepository).findByUserIdAndEventId(user.getId(), event.getId());
  }

  @Test
  void completeEventDuplicateCompleteTest() {
    //given
    userEvent.participateByUser();
    userEvent.completeByBusiness();
    when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
    when(userEventRepository.findByUserIdAndEventId(user.getId(), event.getId())).thenReturn(
        Optional.of(userEvent));

    //when
    assertThrows(AlreadyParticipateException.class,
        () -> userEventService.completeEventByBusiness(user, event.getId()));

    //then
    verify(eventRepository).findById(event.getId());
    verify(userEventRepository).findByUserIdAndEventId(user.getId(), event.getId());
  }

}
