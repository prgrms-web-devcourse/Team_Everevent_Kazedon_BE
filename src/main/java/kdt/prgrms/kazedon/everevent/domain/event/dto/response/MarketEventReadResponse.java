package kdt.prgrms.kazedon.everevent.domain.event.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class MarketEventReadResponse {

    private Page<MarketEvent> events;

}
