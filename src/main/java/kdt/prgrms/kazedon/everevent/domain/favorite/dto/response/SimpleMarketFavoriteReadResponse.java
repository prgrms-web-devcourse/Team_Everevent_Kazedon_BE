package kdt.prgrms.kazedon.everevent.domain.favorite.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public class SimpleMarketFavoriteReadResponse {

  private Page<SimpleMarketFavorite> markets;

  @JsonProperty("markets")
  public Page<SimpleMarketFavorite> getMarkets() {
    return markets;
  }
}
