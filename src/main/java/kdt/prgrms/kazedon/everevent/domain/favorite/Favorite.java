package kdt.prgrms.kazedon.everevent.domain.favorite;

import javax.persistence.*;
import kdt.prgrms.kazedon.everevent.domain.common.BaseTimeEntity;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite extends BaseTimeEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="market_id", referencedColumnName = "id")
  private Market market;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_id", referencedColumnName = "id")
  private User user;

  @Builder
  public Favorite(Market market, User user) {
    this.market = market;
    this.user = user;
  }
}
