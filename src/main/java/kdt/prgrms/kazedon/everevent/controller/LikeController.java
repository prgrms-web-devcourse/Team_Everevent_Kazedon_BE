package kdt.prgrms.kazedon.everevent.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import kdt.prgrms.kazedon.everevent.configures.auth.AuthUser;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;

  @PostMapping("/likes/events/{eventId}")
  public ResponseEntity<Void> addLike(@AuthUser User user,
      @PathVariable Long eventId){
    likeService.addLike(user, eventId);
    return ResponseEntity.created(linkTo(LikeController.class).toUri()).build();
  }

  @DeleteMapping("/likes/events/{eventId}")
  public ResponseEntity<Void> deleteLike(@AuthUser User user,
      @PathVariable Long eventId){
    likeService.deleteLike(user, eventId);
    return ResponseEntity.noContent().build();
  }
}
