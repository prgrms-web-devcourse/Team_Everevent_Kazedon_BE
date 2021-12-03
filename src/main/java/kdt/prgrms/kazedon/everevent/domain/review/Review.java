package kdt.prgrms.kazedon.everevent.domain.review;

import javax.persistence.*;
import kdt.prgrms.kazedon.everevent.domain.common.BaseTimeEntity;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.user.User;
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
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", referencedColumnName = "id")
  private Event event;

  @Column(name = "picture_url", nullable = false, length = 50)
  private String pictureUrl;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  private String description;

  @Builder
  public Review(User user, Event event, String pictureUrl, String description) {
    this.user = user;
    this.event = event;
    this.pictureUrl = pictureUrl;
    this.description = description;
  }
}
