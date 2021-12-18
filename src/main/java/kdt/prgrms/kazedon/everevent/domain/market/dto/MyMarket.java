package kdt.prgrms.kazedon.everevent.domain.market.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyMarket {
    private long marketId;
    private String description;
    private int eventCount;
    private int likeCount;
    private int reviewCount;
}
