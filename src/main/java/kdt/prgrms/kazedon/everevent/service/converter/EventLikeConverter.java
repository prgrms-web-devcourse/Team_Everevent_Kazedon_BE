package kdt.prgrms.kazedon.everevent.service.converter;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.like.EventLike;
import kdt.prgrms.kazedon.everevent.domain.like.dto.response.SimpleEventLike;
import kdt.prgrms.kazedon.everevent.domain.like.dto.response.SimpleEventLikeReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class EventLikeConverter {

  public SimpleEventLikeReadResponse convertToSimpleEventLikeReadResponse(
      Page<SimpleEventLike> simpleEventLikes) {
    return SimpleEventLikeReadResponse.builder()
        .events(simpleEventLikes)
        .build();
  }

  public EventLike convertToEventLike(User user, Event event) {
    return EventLike.builder()
        .user(user)
        .event(event)
        .build();
  }

}
