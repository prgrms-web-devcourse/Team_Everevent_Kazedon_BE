package kdt.prgrms.kazedon.everevent.domain.market.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailMarketReadResponse {

  private Long marketId;
  private String name;
  private String description;
  private String address;

}
