package kdt.prgrms.kazedon.everevent.common;

import java.util.Collections;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockAuthUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockAuthUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockAuthUser mockAuthUser) {
    User user = new User(
        mockAuthUser.id(),
        mockAuthUser.email(),
        mockAuthUser.password(),
        mockAuthUser.nickname(),
        mockAuthUser.location(),
        mockAuthUser.roles().name()
    );

    Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getEmail(), Collections.singletonList(new SimpleGrantedAuthority(user.getRoles())));

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(auth);

    return context;
  }
}
