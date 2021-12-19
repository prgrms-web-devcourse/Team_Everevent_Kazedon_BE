package kdt.prgrms.kazedon.everevent.domain.review.dto.request;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWriteRequest {

  @Size(max=1000)
  private String description;

}
