package kdt.prgrms.kazedon.everevent.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import kdt.prgrms.kazedon.everevent.configures.JwtAuthenticationProvider;
import kdt.prgrms.kazedon.everevent.configures.auth.CustomUserDetails;
import kdt.prgrms.kazedon.everevent.domain.user.Authority;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.dto.LoginRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserRestController {

  private final CustomUserDetailService userDetailsService;

  private final BCryptPasswordEncoder passwordEncoder;

  @PostMapping("/signup")
  public ResponseEntity<Long> signUp(@RequestBody SignUpRequest request){
    Long userId = userDetailsService.signUp(encodingPassword(request));
    return new ResponseEntity<>(userId, HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<Long> logout(HttpServletResponse response){
    Cookie cookie = new Cookie("X-AUTH-TOKEN", null);
    cookie.setHttpOnly(true);
    cookie.setSecure(false);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);
    return new ResponseEntity<>(null, HttpStatus.OK);
  }

  public SignUpRequest encodingPassword(SignUpRequest request){
    request.encodingPassword(passwordEncoder.encode(request.getPassword()));
    return request;
  }

  public boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || (authentication instanceof AnonymousAuthenticationToken)) {
      return false;
    }
    return authentication.isAuthenticated();
  }

}
