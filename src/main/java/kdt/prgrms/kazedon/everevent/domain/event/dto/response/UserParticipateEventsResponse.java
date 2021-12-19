package kdt.prgrms.kazedon.everevent.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public class UserParticipateEventsResponse {

  private Page<UserParticipateEvent> events;

  @JsonProperty("events")
  public Page<UserParticipateEvent> getEvents() {
    return events;
  }
}
