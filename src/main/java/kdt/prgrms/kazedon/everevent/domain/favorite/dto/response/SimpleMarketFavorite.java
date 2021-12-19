package kdt.prgrms.kazedon.everevent.domain.favorite.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class SimpleMarketFavorite {

  private Long marketId;
  private String name;
  private int favoriteCount;

  @JsonProperty("marketId")
  public Long getMarketId() {
    return marketId;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("favoriteCount")
  public int getFavoriteCount() {
    return favoriteCount;
  }

}
