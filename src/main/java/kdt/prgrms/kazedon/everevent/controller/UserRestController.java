package kdt.prgrms.kazedon.everevent.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kdt.prgrms.kazedon.everevent.configures.JwtAuthenticationProvider;
import kdt.prgrms.kazedon.everevent.domain.user.Authority;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.dto.LoginRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserRestController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @PostMapping(path="login")
  public void login(@RequestBody LoginRequest request, HttpServletResponse response){
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("아이디가 일치하지 않습니다."));
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    String token = jwtAuthenticationProvider.createToken(
        user.getUsername(),
        user.getAuthority().stream().map(Authority::getAuthorityName).toList()
    );
    response.setHeader("X-AUTH-TOKEN", token);

    Cookie cookie = new Cookie("X-AUTH-TOKEN", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    response.addCookie(cookie);

  }

  @PostMapping(path="signup")
  public void signUp(@RequestBody SignUpRequest request){
    userRepository.save(
        new User(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getNickname(),
            "")
        );
  }

  @PostMapping("/logout")
  public void logout(HttpServletResponse response, HttpServletRequest request){
    Cookie cookie = new Cookie("X-AUTH-TOKEN", null);
    cookie.setHttpOnly(true);
    cookie.setSecure(false);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);
  }





}
