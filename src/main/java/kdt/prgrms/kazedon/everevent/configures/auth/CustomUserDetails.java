package kdt.prgrms.kazedon.everevent.configures.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private User user;

  public User getUser() {
    return this.user;
  }

  public CustomUserDetails(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (user.getRoles().isBlank()) {
      return Collections.emptyList();
    }
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    Arrays.stream(user.getRoles().split(","))
        .forEach(auth -> authorities.add(new SimpleGrantedAuthority(auth)));
    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
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
