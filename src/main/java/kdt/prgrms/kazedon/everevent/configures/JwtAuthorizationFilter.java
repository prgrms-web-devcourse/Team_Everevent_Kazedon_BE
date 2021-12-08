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

  public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
      JwtAuthenticationProvider jwtAuthenticationProvider) {
    super(authenticationManager);
    this.jwtAuthenticationProvider = jwtAuthenticationProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String header = request.getHeader("X-AUTH-TOKEN");
    if(header == null) {
      chain.doFilter(request, response);
      return;
    }
    String token = request.getHeader("X-AUTH-TOKEN");
    String userEmail = jwtAuthenticationProvider.getUserPk(token);

    if(userEmail != null) {
      Authentication testAuthentication = jwtAuthenticationProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(testAuthentication);
    }

    chain.doFilter(request, response);
  }
}
