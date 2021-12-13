package kdt.prgrms.kazedon.everevent.domain.review.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimpleReview {

  private Long reviewId;
  private String description;
  private String pictureUrl;
  private Long memberId;
  private String memberNickname;
  private LocalDateTime createdAt;

}
