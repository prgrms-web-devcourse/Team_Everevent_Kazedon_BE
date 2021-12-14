package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.EventPicture;
import kdt.prgrms.kazedon.everevent.domain.event.dto.DetailEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.EventCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventPictureRepository;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.EventConverter;
import kdt.prgrms.kazedon.everevent.service.global.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EventService {

  private final EventRepository eventRepository;

  private final EventPictureRepository eventPictureRepository;

  private final MarketRepository marketRepository;

  private final EventConverter eventConverter;

  private final FileService fileService;

  @Transactional(readOnly = true)
  public SimpleEventReadResponse getEventsByLocation(String location, Pageable pageable) {
    boolean isLike = false;

    Page<SimpleEvent> simpleEvents = eventRepository
        .findByLocation(location, pageable)
        .map(event -> eventConverter.convertToSimpleEvent(event, isLike));
    //      .filter(condition checking if the user likes this event using [event_like] table)

    return eventConverter.convertToSimpleEventReadResponse(simpleEvents);
  }

  @Transactional(readOnly = true)
  public DetailEventReadResponse getEventById(Long id) {
    boolean isLike = false;
    boolean isFavorite = false;
    boolean isParticipated = false;

    Event event = eventRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, id));

    return eventConverter.convertToDetailEventReadResponse(event, isLike, isFavorite,
        isParticipated);
  }

  @Transactional
  public Long createEvent(EventCreateRequest createRequest, List<MultipartFile> files){
    Market market = marketRepository.findById(createRequest.getMarketId())
            .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, createRequest.getMarketId()));

    Event event = eventRepository.save(eventConverter.convertToEvent(createRequest, market));

    files.stream()
        .map(fileService::uploadImage)
        .map(pictureUrl -> EventPicture.builder()
            .url(pictureUrl)
            .event(event)
            .build())
        .forEach(eventPicture -> event.addPicture(eventPictureRepository.save(eventPicture)));


    return event.getId();
  }
}
