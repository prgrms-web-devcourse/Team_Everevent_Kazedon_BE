package kdt.prgrms.kazedon.everevent.domain.user;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
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
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.InvalidUserArgumentException;
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

  public void addAuthority(Authority authority) {
    this.authority.add(authority);
  }

  @Builder
  public User(String email, String password, String nickname, String location) {
    checkEmail(email);
    checkPassword(password);

    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.location = location;
    this.authority = new ArrayList<>();
  }

  public User(SignUpRequest request){
    checkEmail(request.getEmail());
    checkPassword(request.getPassword());

    this.email = request.getEmail();
    this.password = request.getPassword();
    this.nickname = request.getNickname();
    this.location = "";
    this.authority = new ArrayList<>();
    addAuthority(new Authority(this, "ROLE_USER"));
  }

  private void checkEmail(String email) {
    String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    Pattern p = Pattern.compile(regex);
    if (email.length() > 30 || email.isBlank() || !p.matcher(email).matches()) {
      throw new InvalidUserArgumentException(ErrorMessage.INVALID_EMAIL_FORMAT, email);
    }
  }

  private void checkPassword(String password) {
    if (password.length() >= 100 || password.isBlank()) {
      throw new InvalidUserArgumentException(ErrorMessage.INVALID_PASSWORD_FORMAT, password);
    }
  }

}
