package kdt.prgrms.kazedon.everevent.domain.event.dto.request;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateRequest {

  @NotNull
  private String description;
}
