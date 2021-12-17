package kdt.prgrms.kazedon.everevent.domain.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SimpleMarketFavorite {

  private Long marketId;
  private String name;
  private int favoriteCount;

}
