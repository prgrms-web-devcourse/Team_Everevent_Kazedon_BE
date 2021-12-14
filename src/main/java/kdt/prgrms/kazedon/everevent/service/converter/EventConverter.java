package kdt.prgrms.kazedon.everevent.service.converter;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.dto.*;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventConverter {
    public SimpleEventReadResponse convertToSimpleEventReadResponse(Page<SimpleEvent> simpleEvents){
        return SimpleEventReadResponse.builder()
                .simpleEvents(simpleEvents)
                .build();
    }

    public SimpleEvent convertToSimpleEvent(Event event, boolean isLike){
        return SimpleEvent.builder()
                .eventId(event.getId())
                .eventName(event.getName())
                .expiredAt(event.getExpiredAt())
                .marketName(event.getMarket().getName())
                .likeCount(event.getLikeCount())
                .reviewCount(event.getReviewCount())
                .isLike(isLike)
                .remainingParticipants(event.getMaxParticipants()- event.getParticipantCount())
                .build();
    }

    public DetailEventReadResponse convertToDetailEventReadResponse(Event event,
                                                                    boolean isLike,
                                                                    boolean isFavorite,
                                                                    boolean isParticipated,
                                                                    List<String> pictures){
        return DetailEventReadResponse.builder()
                .eventName(event.getName())
                .expriedAt(event.getExpiredAt())
                .marketName(event.getMarket().getName())
                .marketDescription(event.getMarket().getDescription())
                .isLike(isLike)
                .isFavorite(isFavorite)
                .isParticipated(isParticipated)
                .pictures(pictures)
                .build();
    }

    public Event convertToEvent(EventCreateRequest createRequest, Market market){
        return Event.builder()
                .market(market)
                .name(createRequest.getName())
                .expiredAt(createRequest.getExpiredAt())
                .description(createRequest.getDescription())
                .maxParticipants(createRequest.getMaxParticipants())
                .build();
    }

    public MarketEventReadRequest convertToMarketEventReadRequest(Page<MarketEvent> marketEvents){
        return MarketEventReadRequest.builder()
                .mareketEvents(marketEvents)
                .build();
    }

    public MarketEvent convertToMarketEvent(Event event){
        return MarketEvent.builder()
                .eventId(event.getId())
                .expiredAt(event.getExpiredAt())
                .eventName(event.getName())
                .marketName(event.getMarket().getName())
                .likeCount(event.getLikeCount())
                .reviewCount(event.getReviewCount())
                .build();
    }
}
