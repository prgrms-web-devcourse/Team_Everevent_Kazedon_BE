package kdt.prgrms.kazedon.everevent.domain.market;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kdt.prgrms.kazedon.everevent.domain.common.BaseTimeEntity;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "market")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Market extends BaseTimeEntity {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id", unique = true)
  private User user;

  @Column(nullable = false, length = 20)
  private String name;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false, length = 200)
  private String address;

  @Column(nullable = false)
  private int favoriteCount;

  @Formula("(select count(1) from Event_Like el "
      + "join event e on e.id = el.event_id "
      + "where e.market_id = id)")
  private int likeCount;

  @Formula("(select count(1) from Review r "
      + "join event e on e.id = r.event_id "
      + "where e.market_id = id)")
  private int reviewCount;

  @Formula("(select count(1) from Event e where e.market_id = id)")
  private int eventCount;

  public void plusOneFavorite() {
    this.favoriteCount++;
  }

  public void minusOneFavorite() {
    this.favoriteCount--;
  }

  @Builder
  public Market(User user, String name, String description, String address) {
    this.user = user;
    this.name = name;
    this.description = description;
    this.address = address;
    this.favoriteCount = 0;
  }
}
