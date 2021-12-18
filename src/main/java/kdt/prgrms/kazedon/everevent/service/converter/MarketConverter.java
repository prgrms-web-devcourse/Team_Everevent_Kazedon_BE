package kdt.prgrms.kazedon.everevent.service.converter;

import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MyMarket;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class MarketConverter {
    public MarketReadResponse convertToMarketReadResponse(MyMarket myMarket){
        return MarketReadResponse.builder()
                .myMarket(myMarket)
                .build();
    }

    public MyMarket convertToSimpleMarket(Market market){
        return MyMarket.builder()
                .marketId(market.getId())
                .description(market.getDescription())
                .eventCount(market.getEventCount())
                .likeCount(market.getLikeCount())
                .reviewCount(market.getReviewCount())
                .build();
    }

    public Market convertToMarket(MarketCreateRequest createRequest, User user){
        return Market.builder()
                .user(user)
                .name(createRequest.getName())
                .description(createRequest.getDescription())
                .address(createRequest.getAddress())
                .build();
    }
}
