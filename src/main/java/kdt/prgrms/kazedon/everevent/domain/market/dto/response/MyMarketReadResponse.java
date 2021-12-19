package kdt.prgrms.kazedon.everevent.domain.market.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyMarketReadResponse {

    private long marketId;
    private String name;
    private String description;
    private int eventCount;
    private int likeCount;
    private int reviewCount;

}
