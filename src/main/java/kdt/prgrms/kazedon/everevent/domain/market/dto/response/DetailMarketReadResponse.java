package kdt.prgrms.kazedon.everevent.domain.market.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class DetailMarketReadResponse {

  private Long marketId;
  private String name;
  private String description;
  private String address;

  @JsonProperty("marketId")
  public Long getMarketId() {
    return marketId;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("address")
  public String getAddress() {
    return address;
  }

}
