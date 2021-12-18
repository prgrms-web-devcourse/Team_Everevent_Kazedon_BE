package kdt.prgrms.kazedon.everevent.service.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.EventPicture;
import kdt.prgrms.kazedon.everevent.domain.event.dto.DetailEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.DetailEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.EventCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.event.dto.MarketEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.MarketEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.UserParticipateEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.UserParticipateEventsResponse;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

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
                .pictureUrl(getAnyPictureUrls(event.getEventPictures()).orElse(""))
                .build();
    }

    public DetailEventReadResponse convertToDetailEventReadResponse(DetailEvent detailEvent,
        List<String> pictures) {
        return DetailEventReadResponse.builder()
            .eventName(detailEvent.getEventName())
            .expriedAt(detailEvent.getExpriedAt())
            .marketName(detailEvent.getMarketName())
            .marketDescription(detailEvent.getMarketDescription())
            .like(detailEvent.isLike())
            .favorite(detailEvent.isFavorite())
            .participated(detailEvent.isParticipated())
            .pictures(pictures)
            .build();
    }

    public Event convertToEvent(EventCreateRequest createRequest, Market market) {
        return Event.builder()
            .market(market)
            .name(createRequest.getName())
            .expiredAt(createRequest.getExpiredAt())
            .description(createRequest.getDescription())
            .maxParticipants(createRequest.getMaxParticipants())
            .eventPictures(new ArrayList<>())
            .build();
    }

    public UserParticipateEventsResponse convertToUserParticipateEventsResponse(
        Page<UserParticipateEvent> userParticipateEventResponses) {
        return UserParticipateEventsResponse.builder()
            .userParticipateEventResponses(userParticipateEventResponses)
            .build();
    }

    public UserParticipateEvent convertToUserParticipateEvent(Event event,
        boolean isLike, boolean isParticipated) {
        return UserParticipateEvent.builder()
            .eventId(event.getId())
            .expiredAt(event.getExpiredAt())
            .name(event.getName())
            .marketName(event.getMarket().getName())
            .likeCount(event.getLikeCount())
            .reviewCount(event.getReviewCount())
            .like(isLike)
            .participated(isParticipated)
            .build();
    }

    private Optional<String> getAnyPictureUrls(List<EventPicture> eventPictures) {
        return eventPictures.stream()
            .map(EventPicture::getUrl)
            .findAny();
    }

    private List<String> getPictureUrls(List<EventPicture> eventPictures) {
        return eventPictures.stream()
                .map(EventPicture::getUrl)
                .toList();
    }

    public MarketEventReadResponse convertToMarketEventReadResponse(Page<MarketEvent> marketEvents){
        return MarketEventReadResponse.builder()
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
