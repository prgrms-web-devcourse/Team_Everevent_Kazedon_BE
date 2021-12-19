package kdt.prgrms.kazedon.everevent.service.converter;

import kdt.prgrms.kazedon.everevent.domain.market.dto.DetailMarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.market.dto.SimpleMarket;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class MarketConverter {
    public MarketReadResponse convertToMarketReadResponse(Page<SimpleMarket> simpleMarkets){
        return MarketReadResponse.builder()
                .simpleMarkets(simpleMarkets)
                .build();
    }

    public SimpleMarket convertToSimpleMarket(Market market, int eventCount, int reviewCount){
        return SimpleMarket.builder()
                .marketId(market.getId())
                .description(market.getDescription())
                .eventCount(eventCount)
                .favoriteCount(market.getFavoriteCount())
                .reviewCount(reviewCount)
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

  public DetailMarketReadResponse convertToDetailMarketReadResponse(Market market) {
        return DetailMarketReadResponse.builder()
                .marketId(market.getId())
                .name(market.getName())
                .description(market.getDescription())
                .address(market.getAddress())
                .build();
  }
}
