package kdt.prgrms.kazedon.everevent.domain.user;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kdt.prgrms.kazedon.everevent.domain.common.BaseTimeEntity;
import kdt.prgrms.kazedon.everevent.domain.user.dto.request.SignUpRequest;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.InvalidUserArgumentException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity {

  private static final String SPLIT = ",";
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");

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

  @Column(length = 200)
  private String roles;

  @Builder
  public User(String email, String password, String nickname, String location) {
    checkEmail(email);

    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.location = location;
    this.roles = UserType.ROLE_USER.name();
  }

  public void changePassword(String password) {
    this.password = password;
  }

  public void changeNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getRoles() {
    return this.roles;
  }

  public void addAuthority(UserType userType) {
    String[] addRoles = this.roles.split(SPLIT);
    if (isExistRoles(userType, addRoles)) {
      return;
    }

    this.roles = MessageFormat.format("{0},{1}", this.roles, userType.name());
  }

  private boolean isExistRoles(UserType userType, String[] addRoles) {
    return Arrays.stream(addRoles).anyMatch(type -> type.equals(userType.name()));
  }

  private void checkEmail(String email) {
    if (email.length() > 30 || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches()) {
      throw new InvalidUserArgumentException(ErrorMessage.INVALID_EMAIL_FORMAT, email);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
