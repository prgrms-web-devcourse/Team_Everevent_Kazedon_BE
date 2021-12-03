package kdt.prgrms.kazedon.everevent.domain.event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import kdt.prgrms.kazedon.everevent.domain.common.BaseTimeEntity;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseTimeEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "market_id", referencedColumnName = "id")
  private Market market;

  @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EventPicture> eventPictures = new ArrayList<>();

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "expired_at", nullable = false)
  private LocalDateTime expiredAt;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(name = "like_count", nullable = false)
  private int likeCount;

  @Column(name = "review_count", nullable = false)
  private int reviewCount;

  @Column(name = "remaining_participants", nullable = false)
  private int remainingParticipants;

  @Builder
  public Event(Market market, String name, LocalDateTime expiredAt, String description,int remainingParticipants) {
    this.market = market;
    this.name = name;
    this.expiredAt = expiredAt;
    this.description = description;
    this.remainingParticipants = remainingParticipants;
    this.likeCount = 0;
    this.reviewCount = 0;
  }

}
