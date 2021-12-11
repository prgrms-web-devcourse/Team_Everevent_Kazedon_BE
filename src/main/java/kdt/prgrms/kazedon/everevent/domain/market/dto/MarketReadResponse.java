package kdt.prgrms.kazedon.everevent.domain.market.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class MarketReadResponse {
    private Page<SimpleMarket> simpleMarkets;
}
