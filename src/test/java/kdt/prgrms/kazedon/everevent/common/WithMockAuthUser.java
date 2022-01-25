package kdt.prgrms.kazedon.everevent.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import kdt.prgrms.kazedon.everevent.domain.user.UserType;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAuthUserSecurityContextFactory.class)
public @interface WithMockAuthUser {

  long id() default 1L;

  String email() default "password";

  String password() default "Password123!";

  String nickname() default "kazedon";

  String location() default "경기도 남양주시 와부읍";

  UserType roles() default UserType.ROLE_USER;
}
