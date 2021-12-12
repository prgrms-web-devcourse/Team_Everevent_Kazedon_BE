package kdt.prgrms.kazedon.everevent.configures;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kdt.prgrms.kazedon.everevent.configures.auth.CustomUserDetails;
import kdt.prgrms.kazedon.everevent.domain.user.Authority;
import kdt.prgrms.kazedon.everevent.domain.user.dto.LoginRequest;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  public JwtAuthenticationFilter(
      AuthenticationManager authenticationManager,
      JwtAuthenticationProvider jwtAuthenticationProvider) {
    this.authenticationManager = authenticationManager;
    this.jwtAuthenticationProvider = jwtAuthenticationProvider;
  }

  @SneakyThrows
  @Override
  public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {

    ObjectMapper objectMapper = new ObjectMapper();
    LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            (loginRequest != null) ? loginRequest.getEmail() : null,
            (loginRequest != null) ? loginRequest.getPassword() : null);

    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) {

    CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
    String token = jwtAuthenticationProvider.createToken(
        userDetails.getUser().getEmail(),
        userDetails.getUser().getAuthority().stream().map(Authority::getAuthorityName).toList());
    response.setHeader("X-AUTH-TOKEN", token);
    Cookie cookie = new Cookie("X-AUTH-TOKEN", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    response.addCookie(cookie);
  }
}
