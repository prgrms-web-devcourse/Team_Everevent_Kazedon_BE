package kdt.prgrms.kazedon.everevent.domain.review;

import javax.persistence.*;
import kdt.prgrms.kazedon.everevent.domain.common.BaseTimeEntity;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.exception.review.InvalidReviewArgumentException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id")
  private Event event;

  @Column(length = 50)
  private String pictureUrl;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;

  @Builder
  public Review(User user, Event event, String pictureUrl, String description) {
    checkPictureUrl(pictureUrl);
    checkDescrption(description);

    this.user = user;
    this.event = event;
    this.pictureUrl = pictureUrl;
    this.description = description;
  }

  private void checkPictureUrl(String pictureUrl) {
    if(pictureUrl!= null && pictureUrl.length()>50)
      throw new InvalidReviewArgumentException("이미지 URL(pictureUrl)");
  }

  private void checkDescrption(String description) {
    if(description!= null && description.length()>1000)
      throw new InvalidReviewArgumentException("댓글 내용(description)");
  }

}
