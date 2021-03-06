package kdt.prgrms.kazedon.everevent.service;

import java.util.List;
import java.util.stream.Collectors;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.EventPicture;
import kdt.prgrms.kazedon.everevent.domain.event.dto.request.EventCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.event.dto.request.EventUpdateRequest;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.DetailEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.DetailEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.MarketEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.MarketEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.SimpleEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.SimpleEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.UserParticipateEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.UserParticipateEventsResponse;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventPictureRepository;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.like.repository.EventLikeRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.userevent.UserEvent;
import kdt.prgrms.kazedon.everevent.domain.userevent.repository.UserEventRepository;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.exception.UnAuthorizedException;
import kdt.prgrms.kazedon.everevent.service.converter.EventConverter;
import kdt.prgrms.kazedon.everevent.service.global.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EventService {

  private final EventRepository eventRepository;
  private final EventPictureRepository eventPictureRepository;
  private final MarketRepository marketRepository;
  private final EventLikeRepository likeRepository;
  private final UserEventRepository userEventRepository;
  private final EventConverter eventConverter;
  private final FileService fileService;

  @Transactional(readOnly = true)
  public SimpleEventReadResponse getEventsByLocation(String location, User user,
      Pageable pageable) {
    Page<SimpleEvent> simpleEvents;
    if(user == null) {
      simpleEvents = eventRepository.findByLocation(location, pageable);
    } else {
      simpleEvents = eventRepository.findByLocation(location, user.getId(), pageable);
    }

    return eventConverter.convertToSimpleEventReadResponse(simpleEvents);
  }

  @Transactional(readOnly = true)
  public DetailEventReadResponse getEventById(Long eventId, User user) {
    DetailEvent detailEvent = eventRepository.findDetailEventById(eventId, user.getId())
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, eventId));
    List<String> pictureUrls = eventPictureRepository.findEventPictureUrlsByEventId(
        eventId);
    return eventConverter.convertToDetailEventReadResponse(detailEvent, pictureUrls);
  }

  @Transactional(readOnly = true)
  public MarketEventReadResponse getEventsByMarket(Long marketId, Pageable pageable) {
    Market market = marketRepository.findById(marketId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, marketId));

    Page<MarketEvent> marketEvents = eventRepository
        .findByMarket(market, pageable)
        .map(eventConverter::convertToMarketEvent);

    return eventConverter.convertToMarketEventReadResponse(marketEvents);
  }

  @Transactional
  public Long createEvent(EventCreateRequest createRequest, List<MultipartFile> files, User user) {
    if (!marketRepository.isPossibleToCreateEvent(createRequest.getMarketId(), user.getId())){
      throw new UnAuthorizedException(ErrorMessage.UNAUTHORIZED_CREATE_EVENT, user.getId());
    }

    Market market = marketRepository.findById(createRequest.getMarketId())
        .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED,
            createRequest.getMarketId()));

    Event event = eventRepository.save(eventConverter.convertToEvent(createRequest, market));

    if (!CollectionUtils.isEmpty(files)) {
      saveFiles(files, event);
    }

    return event.getId();
  }

  private void saveFiles(List<MultipartFile> files, Event event) {
    files.stream()
        .map(fileService::uploadImage)
        .map(pictureUrl -> EventPicture.builder()
            .url(pictureUrl)
            .event(event)
            .build())
        .forEach(eventPicture -> event.addPicture(eventPictureRepository.save(eventPicture)));
  }

  @Transactional(readOnly = true)
  public UserParticipateEventsResponse getEventsParticipatedByUser(Long userId, Pageable pageable) {
    List<Event> events = userEventRepository.findAllByUserId(userId).stream().map(
        UserEvent::getEvent).collect(Collectors.toList());
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), events.size());
    Page<Event> pageEvents = new PageImpl<>(events.subList(start, end), pageable, events.size());

    Page<UserParticipateEvent> userParticipateEvent = pageEvents.map(event -> {
      boolean isLike = likeRepository.findByUserIdAndEventId(userId, event.getId()).isPresent();
      boolean isParticipated = userEventRepository.findByUserIdAndEventId(userId,
          event.getId()).get().isCompleted();
      return eventConverter.convertToUserParticipateEvent(event, isLike, isParticipated);
    });
    return eventConverter.convertToUserParticipateEventsResponse(userParticipateEvent);
  }

  @Transactional
  public void update(Long eventId, User user, EventUpdateRequest eventUpdateRequest) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, eventId));
    if (event.getMarket().getUser().getId() != user.getId()) {
      throw new UnAuthorizedException(ErrorMessage.UNAUTHORIZED_USER, user.getId());
    }
    event.modifyDescription(eventUpdateRequest.getDescription());
    eventRepository.save(event);
  }
}
