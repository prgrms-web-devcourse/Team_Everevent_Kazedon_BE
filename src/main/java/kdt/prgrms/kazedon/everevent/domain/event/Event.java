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
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id")
  private Market market;

  @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EventPicture> eventPictures = new ArrayList<>();

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false)
  private LocalDateTime expiredAt;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private int likeCount;

  @Column(nullable = false)
  private int reviewCount;

  @Column(nullable = false)
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
