package kdt.prgrms.kazedon.everevent.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.domain.userevent.UserEvent;
import kdt.prgrms.kazedon.everevent.domain.userevent.repository.UserEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserEventServiceTest {

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
  @InjectMocks
  private UserEventService userEventService;
  @Mock
  private EventRepository eventRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserEventRepository userEventRepository;

  @Test
  void participateEventByUser() {
    //Given
    userEvent.participateByUser();
    when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
    when(userEventRepository.existsUserEventsByUserIdAndEventId(user.getId(),
        event.getId())).thenReturn(false);
    when(userEventRepository.save(any())).thenReturn(userEvent);

    //When
    userEventService.participateEventByUser(user, event.getId());

    //Then
    verify(eventRepository).findById(event.getId());
    verify(userEventRepository).existsUserEventsByUserIdAndEventId(user.getId(), event.getId());
    verify(userEventRepository).save(any());
  }

  @Test
  void completeEventByBusiness() {
    //Given
    userEvent.participateByUser();
    when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
    when(userEventRepository.findByUserIdAndEventId(user.getId(), event.getId())).thenReturn(
        Optional.of(userEvent));

    //When
    userEventService.completeEventByBusiness(user, event.getId());

    //Then
    verify(eventRepository).findById(event.getId());
    verify(userEventRepository).findByUserIdAndEventId(user.getId(), event.getId());
  }
}
