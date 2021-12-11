package kdt.prgrms.kazedon.everevent.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request){
    Long userId = userService.signUp(request);
    URI uri = linkTo(UserController.class).slash("login").toUri();
    return ResponseEntity.created(uri).build();
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
