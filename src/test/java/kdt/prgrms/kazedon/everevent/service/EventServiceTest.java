package kdt.prgrms.kazedon.everevent.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.dto.*;
import kdt.prgrms.kazedon.everevent.domain.event.dto.DetailEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.EventCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.event.dto.EventUpdateRequest;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.UserParticipateEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.UserParticipateEventsResponse;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.like.repository.EventLikeRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.userevent.UserEvent;
import kdt.prgrms.kazedon.everevent.domain.userevent.repository.UserEventRepository;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.EventConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventConverter eventConverter;

    @Mock
    private MarketRepository marketRepository;

    @Mock
    private UserEventRepository userEventRepository;

    @Mock
    private EventLikeRepository likeRepository;

    @Mock
    private Pageable pageable;

    private User user = User.builder()
            .email("test-email8@gmail.com")
            .password("test-password")
            .nickname("test-nickname")
            .location("test-location")
            .build();

    private Market market = Market.builder()
            .user(user)
            .name("test-market-name")
            .description("test-description")
            .address("test-market-address")
            .build();

    private Event event = Event.builder()
            .market(market)
            .name("test-event-name")
            .expiredAt(LocalDateTime.now())
            .description("test-description")
            .maxParticipants(5)
            .build();

    private Event anotherEvent = Event.builder()
            .market(market)
            .name("another-test-event-name")
            .expiredAt(LocalDateTime.now().minusDays(2))
            .description("another-test-description")
            .maxParticipants(3)
            .build();

    private SimpleEvent simpleEvent = SimpleEvent.builder()
            .eventId(event.getId())
            .eventName(event.getName())
            .expiredAt(event.getExpiredAt())
            .marketName(event.getMarket().getName())
            .reviewCount(event.getReviewCount())
            .isLike(false)
            .remainingParticipants(event.getMaxParticipants() - event.getParticipantCount())
            .build();

    private SimpleEvent anotherSimpleEvent = SimpleEvent.builder()
            .eventId(anotherEvent.getId())
            .eventName(anotherEvent.getName())
            .expiredAt(anotherEvent.getExpiredAt())
            .marketName(anotherEvent.getMarket().getName())
            .reviewCount(anotherEvent.getReviewCount())
            .isLike(false)
            .remainingParticipants(anotherEvent.getMaxParticipants() - anotherEvent.getParticipantCount())
            .build();

    private EventCreateRequest createRequest = EventCreateRequest.builder()
            .marketId(1L)
            .description("test-event-description")
            .expiredAt(LocalDateTime.now().plusDays(3))
            .maxParticipants(10)
            .name("test-event")
            .build();

    private EventCreateRequest invalidCreateRequest = EventCreateRequest.builder()
            .marketId(Long.MAX_VALUE)
            .description("test-event-description")
            .expiredAt(LocalDateTime.now().plusDays(3))
            .maxParticipants(10)
            .name("test-event")
            .build();

    private DetailEventReadResponse detailEventReadResponse = DetailEventReadResponse.builder()
            .eventName(event.getName())
            .expriedAt(event.getExpiredAt())
            .marketName(market.getName())
            .eventDescription(event.getDescription())
            .marketDescription(market.getDescription())
            .isLike(false)
            .isFavorite(false)
            .isParticipated(false)
            .pictures(new ArrayList<>())
            .build();

    private MarketEvent marketEvent = MarketEvent.builder()
            .eventId(event.getId())
            .expiredAt(event.getExpiredAt())
            .eventName(event.getName())
            .marketName(market.getName())
            .likeCount(event.getLikeCount())
            .reviewCount(event.getReviewCount())
            .build();

    private MarketEvent anotherMarketEvent = MarketEvent.builder()
            .eventId(anotherEvent.getId())
            .expiredAt(anotherEvent.getExpiredAt())
            .eventName(anotherEvent.getName())
            .marketName(market.getName())
            .likeCount(event.getLikeCount())
            .reviewCount(event.getReviewCount())
            .build();

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

    @Test
    void getEvent(){
        //Given
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventConverter.convertToDetailEventReadResponse(event, false, false, false)).thenReturn(detailEventReadResponse);

        //When
        DetailEventReadResponse response = eventService.getEventById(eventId);

        //Then
        verify(eventRepository).findById(eventId);
        verify(eventConverter).convertToDetailEventReadResponse(event, false, false, false);
    }

    @Test
    void getEventUsingInvalidId(){
        //Given
        Long invalidEventId = Long.MAX_VALUE;
        when(eventRepository.findById(invalidEventId)).thenReturn(Optional.empty());

        //When
        assertThrows(NotFoundException.class, () -> eventService.getEventById(invalidEventId));

        //Then
        verify(eventRepository).findById(invalidEventId);
    }

    @Test
    void createEvent(){
        //Given
        Long eventId = 1L;
        Long marketId = createRequest.getMarketId();

        when(marketRepository.findById(marketId)).thenReturn(Optional.of(market));
        when(eventConverter.convertToEvent(createRequest, market)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);

        //When
        eventService.createEvent(createRequest, new ArrayList<>());

        //Then
        verify(marketRepository).findById(marketId);
        verify(eventConverter).convertToEvent(createRequest, market);
        verify(eventRepository).save(event);
    }

    @Test
    void createEventUsingInvalidMarketId(){
        //Given
        Long invalidMarketId = invalidCreateRequest.getMarketId();
        when(marketRepository.findById(invalidMarketId)).thenReturn(Optional.empty());

        //When
        //Then
        assertThrows(NotFoundException.class, () -> eventService.createEvent(invalidCreateRequest, new ArrayList<>()));
        verify(marketRepository).findById(invalidMarketId);
    }

    @Test
    void getEventsByMarket(){
        //Given
        Long marketId = 1L;
        Page<Event> events = new PageImpl<>(List.of(event, anotherEvent));

        when(marketRepository.findById(marketId)).thenReturn(Optional.of(market));
        when(eventRepository.findByMarket(market, pageable)).thenReturn(events);
        when(eventConverter.convertToMarketEvent(event)).thenReturn(marketEvent);
        when(eventConverter.convertToMarketEvent(anotherEvent)).thenReturn(anotherMarketEvent);

        //When
        eventService.getEventsByMarket(marketId, pageable);

        //Then
        verify(marketRepository).findById(marketId);
        verify(eventRepository).findByMarket(market, pageable);
        verify(eventConverter).convertToMarketEvent(event);
        verify(eventConverter).convertToMarketEvent(anotherEvent);
        verify(eventConverter).convertToMarketEventReadResponse(any());
    }

    @Test
    void getEventsByInvalidMarket(){
        //Given
        Long marketId = Long.MAX_VALUE;
        when(marketRepository.findById(marketId)).thenReturn(Optional.empty());

        //When
        //Then
        assertThrows(NotFoundException.class, () -> eventService.getEventsByMarket(marketId, pageable));
        verify(marketRepository).findById(marketId);
    }

    @Test
    public void getEventsParticipatedByUserTest() {
        //Given
        UserEvent userEvent = UserEvent.builder().user(user).event(event).build();
        List<UserEvent> userEvents = List.of(userEvent);
        UserParticipateEvent userParticipateEvent = UserParticipateEvent.builder()
            .eventId(event.getId())
            .name(event.getName())
            .expiredAt(event.getExpiredAt())
            .marketName(event.getMarket().getName())
            .likeCount(0)
            .reviewCount(0)
            .build();
        Page<UserParticipateEvent> userParticipateEventPage = new PageImpl<>(
            List.of(userParticipateEvent), pageable, 1);
        UserParticipateEventsResponse response = UserParticipateEventsResponse.builder()
            .userParticipateEventResponses(userParticipateEventPage)
            .build();

        given(userEventRepository.findAllByUserId(user.getId())).willReturn(userEvents);
        given(eventConverter.convertToUserParticipateEventsResponse(
            any())).willReturn(response);

        //When
        UserParticipateEventsResponse eventsParticipatedByUser = eventService.getEventsParticipatedByUser(
            user.getId(), pageable);

        //Then
        assertThat(eventsParticipatedByUser, is(response));
    }

    @Test
    public void updateTest() {
        //Given
        given(eventRepository.findById(event.getId())).willReturn(Optional.ofNullable(event));
        event.modifyDescription("수정");
        given(eventRepository.save(any())).willReturn(event);
        EventUpdateRequest eventUpdateRequest = EventUpdateRequest.builder().description("수정")
            .build();
        //When
        eventService.update(event.getId(), user.getId(), eventUpdateRequest);

        //Then
        verify(eventRepository).findById(event.getId());
        verify(eventRepository).save(event);
    }
}
