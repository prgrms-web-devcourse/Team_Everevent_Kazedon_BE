package kdt.prgrms.kazedon.everevent.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

  private final CustomUserDetailService userDetailsService;

  private final BCryptPasswordEncoder passwordEncoder;

  @PostMapping("/signup")
  public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request){
    Long userId = userDetailsService.signUp(encodingPassword(request));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(){
    if(isAuthenticated()){
      return ResponseEntity.ok().header("X-AUTH-TOKEN","").build();
    }else{
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  public SignUpRequest encodingPassword(SignUpRequest request){
    request.encodingPassword(passwordEncoder.encode(request.getPassword()));
    return request;
  }

  public boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (authentication != null) && !(authentication instanceof AnonymousAuthenticationToken);
  }

}
