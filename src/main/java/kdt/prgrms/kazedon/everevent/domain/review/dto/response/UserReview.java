package kdt.prgrms.kazedon.everevent.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserReview {

    private Long reviewId;
    private String description;
    private String marketName;
    private String pictureUrl;

}
