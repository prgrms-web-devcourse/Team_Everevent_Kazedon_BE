package kdt.prgrms.kazedon.everevent.service.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.EventPicture;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.DetailEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.DetailEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.request.EventCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.MarketEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.MarketEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.SimpleEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.SimpleEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.UserParticipateEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.UserParticipateEventsResponse;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class EventConverter {

    public SimpleEventReadResponse convertToSimpleEventReadResponse(Page<SimpleEvent> simpleEvents){
        return SimpleEventReadResponse.builder()
                .events(simpleEvents)
                .build();
    }

    public SimpleEvent convertToSimpleEvent(Event event, boolean isLike){
        return SimpleEvent.builder()
                .eventId(event.getId())
                .name(event.getName())
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
            .name(detailEvent.getName())
            .expriedAt(detailEvent.getExpriedAt())
            .marketName(detailEvent.getMarketName())
            .marketDescription(detailEvent.getMarketDescription())
            .isLike(detailEvent.isLike())
            .isFavorite(detailEvent.isFavorite())
            .participateStatus(detailEvent.getParticipateStatus())
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
            .events(userParticipateEventResponses)
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
            .isLike(isLike)
            .isCompleted(isParticipated)
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
                .events(marketEvents)
                .build();
    }

    public MarketEvent convertToMarketEvent(Event event){
        return MarketEvent.builder()
                .eventId(event.getId())
                .expiredAt(event.getExpiredAt())
                .name(event.getName())
                .marketName(event.getMarket().getName())
                .likeCount(event.getLikeCount())
                .reviewCount(event.getReviewCount())
                .build();
    }
}
