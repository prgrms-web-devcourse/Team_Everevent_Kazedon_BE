package kdt.prgrms.kazedon.everevent.domain.like.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public class SimpleEventLikeReadResponse {

  private Page<SimpleEventLike> events;

  @JsonProperty("events")
  public Page<SimpleEventLike> getEvents() {
    return events;
  }

}
