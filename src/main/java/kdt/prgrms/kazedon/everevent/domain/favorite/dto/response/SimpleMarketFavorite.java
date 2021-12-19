package kdt.prgrms.kazedon.everevent.domain.favorite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SimpleMarketFavorite {

  private Long marketId;
  private String name;
  private int favoriteCount;

}
