package kdt.prgrms.kazedon.everevent.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.favorite.Favorite;
import kdt.prgrms.kazedon.everevent.domain.favorite.dto.response.SimpleMarketFavorite;
import kdt.prgrms.kazedon.everevent.domain.favorite.dto.response.SimpleMarketFavoriteReadResponse;
import kdt.prgrms.kazedon.everevent.domain.favorite.repository.FavoriteRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.exception.AlreadyFavoritedException;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.FavoriteConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

  @InjectMocks
  private FavoriteService favoriteService;

  @Mock
  private FavoriteRepository favoriteRepository;

  @Mock
  private MarketRepository marketRepository;

  @Mock
  private FavoriteConverter favoriteConverter;

  private Pageable pageable = Pageable.ofSize(5);

  private User user = User.builder()
      .email("test-email9@gmail.com")
      .password("test-password")
      .nickname("test-nickname")
      .location("test-location")
      .build();

  private Market market = Market.builder()
      .user(user)
      .name("test-market")
      .description("test-description")
      .address("test-market-address")
      .build();

  private Favorite favorite = Favorite.builder()
      .market(market)
      .user(user)
      .build();

  private SimpleMarketFavorite simpleMarketFavorite1 = SimpleMarketFavorite.builder()
      .marketId(market.getId())
      .favoriteCount(1)
      .name(market.getName())
      .build();

  private SimpleMarketFavorite simpleMarketFavorite2 = SimpleMarketFavorite.builder()
      .marketId(market.getId())
      .favoriteCount(1)
      .name(market.getName())
      .build();

  private Page<SimpleMarketFavorite> simpleMarketFavorites =
      new PageImpl<>(List.of(simpleMarketFavorite1, simpleMarketFavorite2), pageable, 2);

  private SimpleMarketFavoriteReadResponse simpleMarketFavoriteReadResponse = SimpleMarketFavoriteReadResponse.builder()
      .markets(simpleMarketFavorites)
      .build();

  @BeforeEach
  void setUp(){
    ReflectionTestUtils.setField(user, "id", 1L);
    ReflectionTestUtils.setField(market, "id", 1L);
    ReflectionTestUtils.setField(favorite, "id", 1L);
  }

  @Test
  void addFavoriteSuccessTest() {
    //given
    when(marketRepository.findById(market.getId())).thenReturn(Optional.of(market));
    when(favoriteRepository.existsFavoriteByUserIdAndMarketId(user.getId(),
        market.getId())).thenReturn(false);
    when(favoriteConverter.convertToFavorite(user, market)).thenReturn(favorite);
    when(favoriteRepository.save(favorite)).thenReturn(favorite);

    //when
    favoriteService.addFavorite(user, market.getId());

    //then
    verify(marketRepository).findById(market.getId());
    verify(favoriteRepository).existsFavoriteByUserIdAndMarketId(user.getId(), market.getId());
    verify(favoriteConverter).convertToFavorite(user, market);
    verify(favoriteRepository).save(favorite);
  }

  @Test
  void addFavoriteNotExistMarketTest() {
    //given
    when(marketRepository.findById(market.getId())).thenReturn(Optional.empty());

    //when
    assertThrows(NotFoundException.class,
        () -> favoriteService.addFavorite(user, market.getId()));

    //then
    verify(marketRepository).findById(market.getId());
  }

  @Test
  void addFavoriteDuplicateFavoriteTest() {
    //given
    when(marketRepository.findById(market.getId())).thenReturn(Optional.of(market));
    when(favoriteRepository.existsFavoriteByUserIdAndMarketId(user.getId(),
        market.getId())).thenReturn(true);

    //when
    assertThrows(AlreadyFavoritedException.class,
        () -> favoriteService.addFavorite(user, market.getId()));

    //then
    verify(marketRepository).findById(market.getId());
    verify(favoriteRepository).existsFavoriteByUserIdAndMarketId(user.getId(), market.getId());
  }

  @Test
  void deleteFavoriteSuccessTest() {
    //given
    when(marketRepository.findById(market.getId())).thenReturn(Optional.of(market));
    when(favoriteRepository.findByUserIdAndMarketId(user.getId(), market.getId())).thenReturn(
        Optional.of(favorite));
    when(favoriteRepository.existsFavoriteByUserIdAndMarketId(user.getId(),
        market.getId())).thenReturn(true);

    //when
    favoriteService.deleteFavorite(user, market.getId());

    //then
    verify(marketRepository).findById(market.getId());
    verify(favoriteRepository).findByUserIdAndMarketId(user.getId(), market.getId());
    verify(favoriteRepository).existsFavoriteByUserIdAndMarketId(user.getId(), market.getId());
  }

  @Test
  void deleteFavoriteNotExistMarketTest() {
    //given
    when(marketRepository.findById(market.getId())).thenReturn(Optional.empty());

    //when
    assertThrows(NotFoundException.class,
        () -> favoriteService.deleteFavorite(user, market.getId()));

    //then
    verify(marketRepository).findById(market.getId());
  }

  @Test
  void deleteFavoriteNotExistFavoriteTest() {
    //given
    when(marketRepository.findById(market.getId())).thenReturn(Optional.of(market));
    when(favoriteRepository.findByUserIdAndMarketId(user.getId(), market.getId())).thenReturn(
        Optional.empty());

    //when
    assertThrows(AlreadyFavoritedException.class,
        () -> favoriteService.deleteFavorite(user, market.getId()));

    //then
    verify(marketRepository).findById(market.getId());
    verify(favoriteRepository).findByUserIdAndMarketId(user.getId(), market.getId());
  }

  @Test
  void deleteFavoriteDuplicateDeleteTest() {
    //given
    when(marketRepository.findById(market.getId())).thenReturn(Optional.of(market));
    when(favoriteRepository.findByUserIdAndMarketId(user.getId(), market.getId())).thenReturn(
        Optional.of(favorite));
    when(favoriteRepository.existsFavoriteByUserIdAndMarketId(user.getId(),
        market.getId())).thenReturn(false);

    //when
    assertThrows(AlreadyFavoritedException.class,
        () -> favoriteService.deleteFavorite(user, market.getId()));

    //then
    verify(marketRepository).findById(market.getId());
    verify(favoriteRepository).findByUserIdAndMarketId(user.getId(), market.getId());
    verify(favoriteRepository).existsFavoriteByUserIdAndMarketId(user.getId(), market.getId());
  }


  @Test
  void getFavoritesSuccessTest() {
    //given
    when(favoriteRepository.findSimpleFavoriteByUserId(user.getId(), pageable)).thenReturn(
        simpleMarketFavorites);
    when(favoriteConverter.convertToSimpleMarketFavoriteReadResponse(
        simpleMarketFavorites)).thenReturn(simpleMarketFavoriteReadResponse);

    //when
    favoriteService.getFavorites(user.getId(), pageable);

    //then
    verify(favoriteRepository).findSimpleFavoriteByUserId(user.getId(), pageable);
    verify(favoriteConverter).convertToSimpleMarketFavoriteReadResponse(simpleMarketFavorites);
  }

}
