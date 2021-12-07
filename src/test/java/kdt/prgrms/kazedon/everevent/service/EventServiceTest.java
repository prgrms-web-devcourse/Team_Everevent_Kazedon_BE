package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.converter.EventConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventConverter eventConverter;

    @Mock
    private Pageable pageable;

    private Market market;

    private User user;

    private Event event;

    private Event anotherEvent;

    private SimpleEvent simpleEvent;

    private SimpleEvent anotherSimpleEvent;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test-email")
                .password("test-password")
                .nickname("test-nickname")
                .location("test-location")
                .build();

        market = Market.builder()
                .user(user)
                .name("test-market-name")
                .description("test-description")
                .address("test-market-address")
                .build();

        event = Event.builder()
                .market(market)
                .name("test-event-name")
                .expiredAt(LocalDateTime.now())
                .description("test-description")
                .maxParticipants(5)
                .build();

        anotherEvent = Event.builder()
                .market(market)
                .name("another-test-event-name")
                .expiredAt(LocalDateTime.now().minusDays(2))
                .description("another-test-description")
                .maxParticipants(3)
                .build();

        simpleEvent = SimpleEvent.builder()
                .eventId(event.getId())
                .eventName(event.getName())
                .expiredAt(event.getExpiredAt())
                .marketName(event.getMarket().getName())
                .reviewCount(event.getReviewCount())
                .isLike(false)
                .remainingParticipants(event.getMaxParticipants() - event.getParticipantCount())
                .build();

        anotherSimpleEvent = SimpleEvent.builder()
                .eventId(anotherEvent.getId())
                .eventName(anotherEvent.getName())
                .expiredAt(anotherEvent.getExpiredAt())
                .marketName(anotherEvent.getMarket().getName())
                .reviewCount(anotherEvent.getReviewCount())
                .isLike(false)
                .remainingParticipants(anotherEvent.getMaxParticipants() - anotherEvent.getParticipantCount())
                .build();
    }

    @Test
    void getEventsByLocation() {
        //Given
        Page<Event> events = new PageImpl<>(List.of(event, anotherEvent));
        String location = "test-location";

        when(eventRepository.findByLocation(location, pageable)).thenReturn(events);
        when(eventConverter.convertToSimpleEvent(event, false)).thenReturn(simpleEvent);
        when(eventConverter.convertToSimpleEvent(anotherEvent, false)).thenReturn(anotherSimpleEvent);

        //When
        SimpleEventReadResponse response = eventService.getEventsByLocation(location, pageable);

        //Then
        verify(eventRepository).findByLocation(location, pageable);
        verify(eventConverter).convertToSimpleEvent(event, false);
        verify(eventConverter).convertToSimpleEvent(anotherEvent, false);
        verify(eventConverter).convertToSimpleEventReadResponse(any());

    }
}