package kdt.prgrms.kazedon.everevent.domain.market.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimpleMarket {
    long marketId;
    String description;
    int eventCount;
    int favoriteCount;
    int reviewCount;
}
