package kdt.prgrms.kazedon.everevent.domain.favorite.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class SimpleMarketFavoriteReadResponse {

  private Page<SimpleMarketFavorite> markets;

}
