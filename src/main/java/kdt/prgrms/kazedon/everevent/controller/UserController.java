package kdt.prgrms.kazedon.everevent.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import kdt.prgrms.kazedon.everevent.configures.JwtAuthenticationProvider;
import kdt.prgrms.kazedon.everevent.configures.auth.AuthUser;
import kdt.prgrms.kazedon.everevent.configures.auth.CustomUserDetails;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.UserParticipateEventsResponse;
import kdt.prgrms.kazedon.everevent.domain.favorite.dto.response.SimpleMarketFavoriteReadResponse;
import kdt.prgrms.kazedon.everevent.domain.like.dto.response.SimpleEventLikeReadResponse;
import kdt.prgrms.kazedon.everevent.domain.review.dto.response.UserReviewReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.dto.request.CheckPasswordRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.request.LoginRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.request.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.request.UserUpdateRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.response.UserInfoResponse;
import kdt.prgrms.kazedon.everevent.domain.user.dto.response.UserReadResponse;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.UnAuthorizedException;
import kdt.prgrms.kazedon.everevent.service.EventService;
import kdt.prgrms.kazedon.everevent.service.FavoriteService;
import kdt.prgrms.kazedon.everevent.service.LikeService;
import kdt.prgrms.kazedon.everevent.service.ReviewService;
import kdt.prgrms.kazedon.everevent.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final EventService eventService;
  private final FavoriteService favoriteService;
  private final ReviewService reviewService;
  private final LikeService likeService;

  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  @PostMapping("/login")
  public ResponseEntity<UserInfoResponse> login(@RequestBody LoginRequest request) {
    UserInfoResponse response = userService.login(request);
    CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    String token = jwtAuthenticationProvider.createToken(
        userDetails.getUsername(),
        userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
    );
    return ResponseEntity.ok().header("X-AUTH-TOKEN", token).body(response);
  }

  @PostMapping("/signup")
  public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest request) {
    userService.signUp(request);
    return ResponseEntity.created(linkTo(UserController.class).slash("login").toUri()).build();
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout() {
    if (isAuthenticated()) {
      return ResponseEntity.ok().header("X-AUTH-TOKEN", "").build();
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @GetMapping("/signup/check")
  public ResponseEntity<Void> checkDuplicate(@RequestParam String type,
      @RequestParam String value) {

    if(type.equals("eamil")){
      userService.checkEmailDuplicate(value);
    }else{
      userService.checkNicknameDuplicate(value);
    }

    return ResponseEntity.ok().build();
  }

  @GetMapping("/members/{memberId}/events")
  public ResponseEntity<UserParticipateEventsResponse> getParticipatedEventsByUser(
      @PathVariable Long memberId,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(eventService.getEventsParticipatedByUser(memberId, pageable));
  }

  @GetMapping("/members")
  public ResponseEntity<UserReadResponse> getUser(@AuthUser User user) {
    return ResponseEntity.ok(userService.getUser(user));
  }

  @PutMapping("/members")
  public ResponseEntity<Void> updateUser(@RequestBody @Valid UserUpdateRequest updateRequest, @AuthUser User user){
    userService.updateUser(updateRequest, user);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/members/{memberId}/favorites/markets")
  public ResponseEntity<SimpleMarketFavoriteReadResponse> getFavorites(@PathVariable Long memberId,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(favoriteService.getFavorites(memberId, pageable));
  }

  @GetMapping("/members/{memberId}/likes/events")
  public ResponseEntity<SimpleEventLikeReadResponse> getLikes(@PathVariable Long memberId,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(likeService.getLikes(memberId, pageable));
  }

  @GetMapping("/members/{memberId}/reviews")
  public ResponseEntity<UserReviewReadResponse> getReivews(@PathVariable Long memberId,
                                                           @AuthUser User user,
                                                           @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
    return ResponseEntity.ok(reviewService.getUserReviews(user, memberId, pageable));
  }

  @GetMapping("/members/check/token")
  public ResponseEntity<UserInfoResponse> validateToken(HttpServletRequest request,
      @AuthUser User user) {
    if (!isAuthenticated()) {
      throw new UnAuthorizedException(ErrorMessage.INVALID_TOKEN,
          request.getHeader("X-AUTH-TOKEN"));
    }
    return ResponseEntity.ok().body(userService.getUserInfo(user));
  }

  @PostMapping("/members/check/password")
  public ResponseEntity<Void> checkPassword(@RequestBody CheckPasswordRequest request,
      @AuthUser User user) {
    userService.checkPassword(user, request.getPassword());
    return ResponseEntity.ok().build();
  }

  public boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (authentication != null) && !(authentication instanceof AnonymousAuthenticationToken);
  }

}
