package kdt.prgrms.kazedon.everevent.service;

import com.sun.jdi.request.DuplicateRequestException;
import kdt.prgrms.kazedon.everevent.domain.favorite.Favorite;
import kdt.prgrms.kazedon.everevent.domain.favorite.repository.FavoriteRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.AlreadyFavoritedException;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

  private final FavoriteRepository favoriteRepository;

  private final UserRepository userRepository;

  private final MarketRepository marketRepository;

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
    return favorite.getId();
  }

  @Transactional
  public Long deleteFavorite(Long userId, Long marketId){
    Favorite favorite = favoriteRepository.findByUserAndMarket(userId, marketId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, marketId));
    if(favoriteRepository.existsFavoriteByUserIdAndMarketId(userId, marketId)){
      favoriteRepository.deleteById(favorite.getId());
      return favorite.getId();
    }
    throw new AlreadyFavoritedException(ErrorMessage.DUPLICATE_NOT_FAVORITE_MARKET,userId);
  }

}
