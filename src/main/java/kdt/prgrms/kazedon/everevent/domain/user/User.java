package kdt.prgrms.kazedon.everevent.domain.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import kdt.prgrms.kazedon.everevent.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity implements UserDetails {

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

  @Column(nullable = false)
  private int likeCount;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "authority_id", referencedColumnName = "id")
  private List<Authority> authority;


  public void addAuthority(Authority authority) {
    this.authority.add(authority);
  }

  @Builder
  public User(String email, String password, String nickname, String location) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.location = location;
    this.likeCount = 0;
    this.authority = new ArrayList<>();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authority.stream()
        .map((Authority role) ->
            new SimpleGrantedAuthority(role.getAuthorityName()))
        .collect(Collectors.toList());
  }

  @Override
  public String getUsername() {
    return this.nickname;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
