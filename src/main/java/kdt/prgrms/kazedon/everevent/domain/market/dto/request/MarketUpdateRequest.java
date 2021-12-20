package kdt.prgrms.kazedon.everevent.domain.market.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketUpdateRequest {

    @NotBlank
    private String description;

}
