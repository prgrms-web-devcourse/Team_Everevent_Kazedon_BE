package kdt.prgrms.kazedon.everevent.domain.market.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketCreateRequest {

    @Size(max = 20)
    private String name;

    @Size(max = 200)
    private String address;

    private String description;
}
