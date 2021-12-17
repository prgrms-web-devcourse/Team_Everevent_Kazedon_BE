package kdt.prgrms.kazedon.everevent.domain.event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
  private int maxParticipants;

  @Column(nullable = false)
  private int participantCount;

  @Column(nullable = false)
  private int likeCount;

  @Column(nullable = false)
  private int reviewCount;

  @Builder
  public Event(Market market, String name, LocalDateTime expiredAt, String description, int maxParticipants, List<EventPicture> eventPictures) {
    this.market = market;
    this.name = name;
    this.expiredAt = expiredAt;
    this.description = description;
    this.maxParticipants = maxParticipants;
    this.participantCount = 0;
    this.likeCount = 0;
    this.reviewCount = 0;
    this.eventPictures = eventPictures;
  }

  public void plusOneLike() {
    this.likeCount++;
  }

  public void minusOneLike() {
    this.likeCount--;
  }

  public void plusOneReviewCount() {
    this.reviewCount++;
  }

  public void modifyDescription(String description) {
    this.description = description;
  }

  public void addPicture(EventPicture eventPicture) {
    this.eventPictures.add(eventPicture);

    if(eventPicture.getEvent() != this)
      eventPicture.setEvent(this);
  }

}
