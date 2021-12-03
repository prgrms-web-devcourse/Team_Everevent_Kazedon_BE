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
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "email", nullable = false, unique = true, length = 30)
  private String email;

  @Column(name = "password", nullable = false, length = 20)
  private String password;

  @Column(name = "nickname", nullable = false, unique = true, length = 20)
  private String nickname;

  @Column(name = "location", nullable = false, length = 200)
  private String location;

  @Column(name = "like_count", nullable = false)
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
