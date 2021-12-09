package kdt.prgrms.kazedon.everevent.configures.auth;

import java.util.ArrayList;
import java.util.Collection;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

  public class CustomUserDetails implements UserDetails {

  private User user;

  public CustomUserDetails(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    user.getAuthority().forEach(auth -> authorities.add(auth::getAuthorityName));
    return authorities;
  }

  public User getUser(){
    return this.user;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getNickname();
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
