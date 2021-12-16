package kdt.prgrms.kazedon.everevent.domain.like.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class SimpleLikeReadResponse {
  private Page<SimpleLike> simpleLikes;
}
