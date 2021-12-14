package kdt.prgrms.kazedon.everevent.domain.event.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class MarketEventReadRequest {
    private Page<MarketEvent> mareketEvents;
}
