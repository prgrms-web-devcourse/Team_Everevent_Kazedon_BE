package kdt.prgrms.kazedon.everevent.domain.user;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authority")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id")
  private User user;

  @Column(nullable = false)
  private String authorityName;

  @Builder
  public Authority(User user, String authorityName) {
    this.user = user;
    this.authorityName = authorityName;
  }

}
