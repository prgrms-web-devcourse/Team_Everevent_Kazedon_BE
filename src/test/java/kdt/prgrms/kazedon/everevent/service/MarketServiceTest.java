package kdt.prgrms.kazedon.everevent.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MyMarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.UserType;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.service.converter.MarketConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class MarketServiceTest {
    @InjectMocks
    private MarketService marketService;

    @Mock
    private MarketRepository marketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MarketConverter marketConverter;

    @Mock
    private UserService userService;

    @Mock
    private Pageable pageable;

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

    private Market anotherMarket = Market.builder()
            .user(user)
            .name("another-test-market")
            .description("another-test-description")
            .address("another-test-market-address")
            .build();

    private MyMarketReadResponse myMarket = MyMarketReadResponse.builder()
            .marketId(1L)
            .description(market.getDescription())
            .eventCount(0)
            .likeCount(market.getFavoriteCount())
            .reviewCount(0)
            .build();

    private MyMarketReadResponse anotherMyMarket = MyMarketReadResponse.builder()
            .marketId(2L)
            .description(anotherMarket.getDescription())
            .eventCount(0)
            .likeCount(anotherMarket.getFavoriteCount())
            .reviewCount(0)
            .build();

    private MarketCreateRequest createRequest = MarketCreateRequest.builder()
            .name("new-test-market")
            .description("new-test-description")
            .address("new-test-address")
            .build();

    @Test
    void createMarket(){
        //Given
        Market newMarket = Market.builder()
                .user(user)
                .name(createRequest.getName())
                .address(createRequest.getAddress())
                .description(createRequest.getDescription())
                .build();

        when(marketConverter.convertToMarket(createRequest, user)).thenReturn(newMarket);
        when(marketRepository.save(newMarket)).thenReturn(newMarket);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userService.changeAuthorityToBusiness(user.getEmail())).thenReturn(
            UserType.ROLE_BUSINESS);

        //When
        marketService.createMarket(createRequest, user.getId());

        //Then
        verify(userRepository).findById(any());
        verify(marketConverter).convertToMarket(createRequest, user);
        verify(marketRepository).save(newMarket);
        verify(userService).changeAuthorityToBusiness(user.getEmail());
    }

    @Test
    void getMarketsByUser(){
        //Given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(marketRepository.findByUser(user)).thenReturn(Optional.of(market));
        when(marketConverter.convertToSimpleMarket(market)).thenReturn(myMarket);

        //When
        marketService.getMarketsByUser(user.getId());

        //Then
        verify(marketRepository).findByUser(user);
        verify(marketConverter).convertToSimpleMarket(market);
    }
}
