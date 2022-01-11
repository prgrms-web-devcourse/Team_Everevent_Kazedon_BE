package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.favorite.Favorite;
import kdt.prgrms.kazedon.everevent.domain.favorite.dto.response.SimpleMarketFavorite;
import kdt.prgrms.kazedon.everevent.domain.favorite.dto.response.SimpleMarketFavoriteReadResponse;
import kdt.prgrms.kazedon.everevent.domain.favorite.repository.FavoriteRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.exception.AlreadyFavoritedException;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.FavoriteConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final MarketRepository marketRepository;
  private final FavoriteConverter favoriteConverter;

  @Transactional
  public void addFavorite(User user, Long marketId){
    Market market = marketRepository.findById(marketId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, marketId));

    if(favoriteRepository.existsFavoriteByUserIdAndMarketId(user.getId(), marketId)){
      throw new AlreadyFavoritedException(ErrorMessage.DUPLICATE_FAVORITE_MARKET,user.getId());
    }

    Favorite favorite = favoriteConverter.convertToFavorite(user, market);
    favoriteRepository.save(favorite);

    market.plusOneFavorite();
  }

  @Transactional
  public void deleteFavorite(User user, Long marketId) {
    Market market = marketRepository.findById(marketId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, marketId));

    Favorite favorite = favoriteRepository.findByUserIdAndMarketId(user.getId(), marketId)
        .orElseThrow(
            () -> new AlreadyFavoritedException(ErrorMessage.FAVORITE_NOT_FOUNDED, marketId));

    if (!favoriteRepository.existsFavoriteByUserIdAndMarketId(user.getId(), marketId)) {
      throw new AlreadyFavoritedException(ErrorMessage.DUPLICATE_NOT_FAVORITE_MARKET,
          favorite.getId());
    }

    market.minusOneFavorite();

    favoriteRepository.deleteById(favorite.getId());
  }

  @Transactional(readOnly = true)
  public SimpleMarketFavoriteReadResponse getFavorites(Long userId, Pageable pageable) {
    Page<SimpleMarketFavorite> simpleMarketFavorites = favoriteRepository
        .findSimpleFavoriteByUserId(userId, pageable);

    return favoriteConverter.convertToSimpleMarketFavoriteReadResponse(simpleMarketFavorites);
  }

}
