package kdt.prgrms.kazedon.everevent.configures;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.MessageFormat;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kdt.prgrms.kazedon.everevent.configures.auth.CustomUserDetails;
import kdt.prgrms.kazedon.everevent.domain.user.dto.request.LoginRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.response.UserInfoResponse;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.service.converter.UserConverter;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  private final UserConverter userConverter;

  public JwtAuthenticationFilter(
      AuthenticationManager authenticationManager,
      JwtAuthenticationProvider jwtAuthenticationProvider,
      UserConverter userConverter) {
    this.authenticationManager = authenticationManager;
    this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    this.userConverter = userConverter;
  }

  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response)
      throws AuthenticationException {

    LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(),
        LoginRequest.class);

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            (loginRequest != null) ? loginRequest.getEmail() : null,
            (loginRequest != null) ? loginRequest.getPassword() : null);

    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) throws IOException {

    CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
    String token = jwtAuthenticationProvider.createToken(
        userDetails.getUsername(),
        userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
    );

    response.setHeader("X-AUTH-TOKEN", token);
    response.setStatus(HttpStatus.OK.value());

    UserInfoResponse userInfoResponse = userConverter.convertToUserInfoResponse(
        userDetails.getUser());
    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    MediaType jsonMimeType = MediaType.APPLICATION_JSON;

    if (jsonConverter.canWrite(userInfoResponse.getClass(), jsonMimeType)) {
      jsonConverter.write(userInfoResponse, jsonMimeType, new ServletServerHttpResponse(response));
    }
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException exception) throws IOException {
    LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(),
        LoginRequest.class);

    String errorMessage = MessageFormat.format(ErrorMessage.LOGIN_FAILED.getMessage(),
        loginRequest.getEmail());
    response.sendError(HttpStatus.UNAUTHORIZED.value(), errorMessage);
  }

}
