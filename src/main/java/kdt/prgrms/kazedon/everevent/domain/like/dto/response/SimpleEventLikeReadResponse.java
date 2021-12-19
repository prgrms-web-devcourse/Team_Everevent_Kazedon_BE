package kdt.prgrms.kazedon.everevent.domain.like.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class SimpleEventLikeReadResponse {

  private Page<SimpleEventLike> events;

}
