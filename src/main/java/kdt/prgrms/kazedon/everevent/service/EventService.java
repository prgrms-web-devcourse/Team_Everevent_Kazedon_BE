package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.dto.*;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.EventConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

  private final EventRepository eventRepository;
  private final EventConverter eventConverter;
  private final MarketRepository marketRepository;

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
    List<String> pictures = new ArrayList<>();

    Event event = eventRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUNDED, id));

    return eventConverter.convertToDetailEventReadResponse(event, isLike, isFavorite,
        isParticipated, pictures);
  }

  @Transactional(readOnly = true)
  public MarketEventReadRequest getEventsByMarket(Long marketId, Pageable pageable){
    Market market = marketRepository.findById(marketId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, marketId));

    Page<MarketEvent> marketEvents = eventRepository
            .findByMarket(market, pageable)
            .map(eventConverter::convertToMarketEvent);

    return eventConverter.convertToMarketEventReadRequest(marketEvents);
  }

  @Transactional
  public Long createEvent(EventCreateRequest createRequest){
    Market market = marketRepository.findById(createRequest.getMarketId())
            .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, createRequest.getMarketId()));
    Event event = eventRepository.save(eventConverter.convertToEvent(createRequest, market));

    return event.getId();
  }
}
