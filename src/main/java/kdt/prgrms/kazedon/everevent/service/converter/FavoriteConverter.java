package kdt.prgrms.kazedon.everevent.service.converter;

import kdt.prgrms.kazedon.everevent.domain.favorite.Favorite;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class FavoriteConverter {

  public Favorite convertToFavorite(User user, Market market) {
    return Favorite.builder()
        .user(user)
        .market(market)
        .build();
  }
}
