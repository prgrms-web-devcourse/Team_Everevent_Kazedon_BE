package kdt.prgrms.kazedon.everevent.domain.market;

import java.time.LocalDateTime;
import javax.persistence.*;
import kdt.prgrms.kazedon.everevent.domain.common.BaseTimeEntity;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "market")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Market extends BaseTimeEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(name = "name", nullable = false, length = 20)
  private String name;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(name = "address", nullable = false, length = 200)
  private String address;

  @Column(name = "favorite_count", nullable = false)
  private int favoriteCount;

  @Builder
  public Market(User user, String name, String description, String address) {
    this.user = user;
    this.name = name;
    this.description = description;
    this.address = address;
    this.favoriteCount = 0;
  }

}
