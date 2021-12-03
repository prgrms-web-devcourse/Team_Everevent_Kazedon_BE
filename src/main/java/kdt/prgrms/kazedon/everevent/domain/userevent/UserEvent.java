package kdt.prgrms.kazedon.everevent.domain.userevent;

import javax.persistence.*;
import kdt.prgrms.kazedon.everevent.domain.common.BaseTimeEntity;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEvent extends BaseTimeEntity {

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

  @Column(name = "is_participated", nullable = false)
  private boolean isParticipated;

  @Builder
  public UserEvent(User user, Event event) {
    this.user = user;
    this.event = event;
    this.isParticipated = false;
  }
}
