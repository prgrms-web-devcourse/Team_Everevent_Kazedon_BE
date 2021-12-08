package kdt.prgrms.kazedon.everevent.service.converter;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.dto.DetailEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEventReadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                .eventId(event.getId())
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
}
