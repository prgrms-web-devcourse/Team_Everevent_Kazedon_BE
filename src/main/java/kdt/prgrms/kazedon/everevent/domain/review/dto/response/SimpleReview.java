package kdt.prgrms.kazedon.everevent.domain.review.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class SimpleReview {

  private Long reviewId;
  private String description;
  private List<String> pictureUrls;
  private Long memberId;
  private String memberNickname;
  private LocalDateTime createdAt;

  @JsonProperty("reviewId")
  public Long getReviewId() {
    return reviewId;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("pictureUrls")
  public List<String> getPictureUrls() {
    return pictureUrls;
  }

  @JsonProperty("memberId")
  public Long getMemberId() {
    return memberId;
  }

  @JsonProperty("memberNickname")
  public String getMemberNickname() {
    return memberNickname;
  }

  @JsonProperty("createdAt")
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

}
