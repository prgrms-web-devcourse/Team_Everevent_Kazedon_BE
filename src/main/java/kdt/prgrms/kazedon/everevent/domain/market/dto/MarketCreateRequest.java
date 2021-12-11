package kdt.prgrms.kazedon.everevent.domain.market.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MarketCreateRequest {

    @Size(max = 20)
    private String name;

    @Size(max = 200)
    private String address;

    private String description;
}
