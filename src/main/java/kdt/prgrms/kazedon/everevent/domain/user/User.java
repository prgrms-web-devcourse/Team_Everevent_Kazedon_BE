package kdt.prgrms.kazedon.everevent.domain.user;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kdt.prgrms.kazedon.everevent.domain.common.BaseTimeEntity;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
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

  @Column(nullable = false, length = 100)
  private String password;

  @Column(nullable = false, unique = true, length = 20)
  private String nickname;

  @Column(nullable = false, length = 200)
  private String location;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "authority_id", referencedColumnName = "id")
  private List<Authority> authority;

  public User(SignUpRequest request) {
    this.email = request.getEmail();
    this.password = request.getPassword();
    this.nickname = request.getNickname();
    this.location = "";
    this.authority = new ArrayList<>();
    changeAuthority(UserType.ROLE_USER);
  }

  @Builder
  public User(String email, String password, String nickname, String location) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.location = location;
    this.authority = new ArrayList<>();
  }

  public void changeAuthority(UserType userType) {
    this.authority.clear();
    this.authority.add(new Authority(this, userType.name()));
  }

}
