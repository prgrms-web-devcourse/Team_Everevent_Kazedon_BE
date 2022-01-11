package kdt.prgrms.kazedon.everevent.service.converter;

import kdt.prgrms.kazedon.everevent.domain.favorite.Favorite;
import kdt.prgrms.kazedon.everevent.domain.favorite.dto.response.SimpleMarketFavorite;
import kdt.prgrms.kazedon.everevent.domain.favorite.dto.response.SimpleMarketFavoriteReadResponse;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.data.domain.Page;

public class FavoriteConverter {

  public Favorite convertToFavorite(User user, Market market) {
    return Favorite.builder()
        .user(user)
        .market(market)
        .build();
  }

  public SimpleMarketFavoriteReadResponse convertToSimpleMarketFavoriteReadResponse(
      Page<SimpleMarketFavorite> simpleMarketFavorites) {
    return SimpleMarketFavoriteReadResponse.builder()
        .markets(simpleMarketFavorites)
        .build();
  }
}
