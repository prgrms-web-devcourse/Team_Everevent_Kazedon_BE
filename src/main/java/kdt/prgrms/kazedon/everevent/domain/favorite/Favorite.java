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
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id")
  private Market market;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id")
  private User user;

  @Builder
  public Favorite(Market market, User user) {
    this.market = market;
    this.user = user;
  }
}
