package kdt.prgrms.kazedon.everevent.configures;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  private static final String TOKEN_HEADER = "X-AUTH-TOKEN";

  public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
      JwtAuthenticationProvider jwtAuthenticationProvider) {
    super(authenticationManager);
    this.jwtAuthenticationProvider = jwtAuthenticationProvider;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain
  ) throws IOException, ServletException {
    if(request.getHeader(TOKEN_HEADER) == null) {
      chain.doFilter(request, response);
      return;
    }

    String token = request.getHeader(TOKEN_HEADER);
    if (jwtAuthenticationProvider.validateToken(token)) {
      Authentication testAuthentication = jwtAuthenticationProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(testAuthentication);
    } else {
      response.setStatus(401);
    }

    chain.doFilter(request, response);
  }

}
