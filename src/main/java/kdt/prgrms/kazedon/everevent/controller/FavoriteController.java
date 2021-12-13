package kdt.prgrms.kazedon.everevent.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import kdt.prgrms.kazedon.everevent.configures.auth.AuthUser;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FavoriteController {
  private final FavoriteService favoriteService;

  @PostMapping("/favorites/markets/{marketId}")
  public ResponseEntity<Void> addFavorite(@AuthUser User user,
      @PathVariable Long marketId){
    Long favoriteId = favoriteService.addFavorite(user.getId(), marketId);
    return ResponseEntity.created(linkTo(FavoriteController.class).toUri()).build();
  }

  @DeleteMapping("/favorites/markets/{marketId}")
  public ResponseEntity<Void> deleteFavorite(@AuthUser User user,
      @PathVariable Long marketId){
    Long deleteId = favoriteService.deleteFavorite(user.getId(), marketId);
    return ResponseEntity.noContent().build();
  }
}
