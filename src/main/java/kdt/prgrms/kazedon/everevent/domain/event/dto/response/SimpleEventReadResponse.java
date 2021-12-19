package kdt.prgrms.kazedon.everevent.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public class SimpleEventReadResponse {

   private Page<SimpleEvent> events;

   @JsonProperty("events")
   public Page<SimpleEvent> getEvents() {
      return events;
   }

}
