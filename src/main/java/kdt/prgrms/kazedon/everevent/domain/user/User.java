package kdt.prgrms.kazedon.everevent.domain.user;

import javax.persistence.*;
import kdt.prgrms.kazedon.everevent.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 30)
  private String email;

  @Column(nullable = false, length = 20)
  private String password;

  @Column(nullable = false, unique = true, length = 20)
  private String nickname;

  @Column(nullable = false, length = 200)
  private String location;

  @Column(nullable = false)
  private int likeCount;

  @Builder
  public User(String email, String password, String nickname, String location) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.location = location;
    this.likeCount = 0;
  }

}
