package kdt.prgrms.kazedon.everevent.domain.favorite.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class SimpleFavoriteReadResponse {
  private Page<SimpleFavorite> simpleFavorites;
}
