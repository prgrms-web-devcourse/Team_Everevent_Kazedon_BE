package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.favorite.Favorite;
import kdt.prgrms.kazedon.everevent.domain.favorite.dto.SimpleMarketFavorite;
import kdt.prgrms.kazedon.everevent.domain.favorite.dto.SimpleMarketFavoriteReadResponse;
import kdt.prgrms.kazedon.everevent.domain.favorite.repository.FavoriteRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.AlreadyFavoritedException;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

  private final FavoriteRepository favoriteRepository;

  private final UserRepository userRepository;

  private final MarketRepository marketRepository;

  @Transactional
  public Long addFavorite(Long userId, Long marketId){
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, userId));
    Market market = marketRepository.findById(marketId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, marketId));
    if(favoriteRepository.existsFavoriteByUserIdAndMarketId(userId, marketId)){
      throw new AlreadyFavoritedException(ErrorMessage.DUPLICATE_FAVORITE_MARKET,userId);
    }
    Favorite favorite = Favorite.builder().user(user).market(market).build();
    favoriteRepository.save(favorite);
    market.plusOneFavorite();
    return favorite.getId();
  }

  @Transactional
  public Long deleteFavorite(Long userId, Long marketId) {
    Market market = marketRepository.findById(marketId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, marketId));
    Favorite favorite = favoriteRepository.findByUserIdAndMarketId(userId, marketId)
        .orElseThrow(
            () -> new AlreadyFavoritedException(ErrorMessage.FAVORITE_NOT_FOUNDED, marketId));
    if (!favoriteRepository.existsFavoriteByUserIdAndMarketId(userId, marketId)) {
      throw new AlreadyFavoritedException(ErrorMessage.DUPLICATE_NOT_FAVORITE_MARKET,
          favorite.getId());
    }
    market.minusOneFavorite();
    favoriteRepository.deleteById(favorite.getId());
    return favorite.getId();

  }

  @Transactional(readOnly = true)
  public SimpleMarketFavoriteReadResponse getFavorites(Long memberId, Pageable pageable) {
    Page<SimpleMarketFavorite> simpleMarketFavorites = favoriteRepository
        .findSimpleFavoriteByUserId(memberId, pageable);

    return SimpleMarketFavoriteReadResponse.builder()
        .simpleMarketFavorites(simpleMarketFavorites)
        .build();
  }
}
