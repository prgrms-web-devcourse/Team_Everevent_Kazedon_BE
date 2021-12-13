package kdt.prgrms.kazedon.everevent.domain.review.dto;

import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewWriteRequest {

  @Size(max=1000)
  private String description;

}
