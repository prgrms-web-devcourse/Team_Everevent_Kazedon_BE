package kdt.prgrms.kazedon.everevent.service;

import java.util.ArrayList;
import java.util.stream.Collectors;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.EventPicture;
import kdt.prgrms.kazedon.everevent.domain.event.dto.*;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventPictureRepository;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.like.repository.EventLikeRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
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
import java.util.List;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EventService {

  private final UserRepository userRepository;
  private final EventRepository eventRepository;
  private final EventPictureRepository eventPictureRepository;
  private final MarketRepository marketRepository;
  private final EventLikeRepository likeRepository;
  private final UserEventRepository userEventRepository;
  private final EventConverter eventConverter;
  private final FileService fileService;

  @Transactional(readOnly = true)
  public SimpleEventReadResponse getEventsByLocation(String location, String userEmail, Pageable pageable) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(()-> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, userEmail));

    Page<SimpleEvent> simpleEvents = eventRepository.findByLocation(location, user.getId(), pageable);

    return eventConverter.convertToSimpleEventReadResponse(simpleEvents);
  }

  @Transactional(readOnly = true)
  public DetailEventReadResponse getEventById(Long id) {
    boolean isLike = false;
    boolean isFavorite = false;
    boolean isParticipated = false;
    List<String> pictures = new ArrayList<>();

    Event event = eventRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, id));

    return eventConverter.convertToDetailEventReadResponse(event, isLike, isFavorite,
        isParticipated);
  }

  @Transactional(readOnly = true)
  public MarketEventReadResponse getEventsByMarket(Long marketId, Pageable pageable){
    Market market = marketRepository.findById(marketId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, marketId));

    Page<MarketEvent> marketEvents = eventRepository
            .findByMarket(market, pageable)
            .map(eventConverter::convertToMarketEvent);

    return eventConverter.convertToMarketEventReadResponse(marketEvents);
  }

  @Transactional
  public Long createEvent(EventCreateRequest createRequest, List<MultipartFile> files) {
    Market market = marketRepository.findById(createRequest.getMarketId())
        .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED,
            createRequest.getMarketId()));

    Event event = eventRepository.save(eventConverter.convertToEvent(createRequest, market));

    if (!CollectionUtils.isEmpty(files))
      saveFiles(files, event);

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
  public void update(Long eventId, Long userId, EventUpdateRequest eventUpdateRequest) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, eventId));
    if (event.getMarket().getUser().getId() != userId) {
      throw new UnAuthorizedException(ErrorMessage.UNAUTHORIZED_USER, userId);
    }
    event.modifyDescription(eventUpdateRequest.getDescription());
    eventRepository.save(event);
  }
}
