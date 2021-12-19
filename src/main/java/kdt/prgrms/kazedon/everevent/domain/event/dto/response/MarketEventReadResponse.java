package kdt.prgrms.kazedon.everevent.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public class MarketEventReadResponse {

    private Page<MarketEvent> events;

    @JsonProperty("events")
    public Page<MarketEvent> getEvents() {
        return events;
    }

}
