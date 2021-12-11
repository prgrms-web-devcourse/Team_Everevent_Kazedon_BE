package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.dto.SimpleMarket;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.service.converter.MarketConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

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
    private Pageable pageable;

    private User user = User.builder()
            .email("test-email")
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

    private SimpleMarket simpleMarket = SimpleMarket.builder()
            .marketId(1L)
            .description(market.getDescription())
            .eventCount(0)
            .favoriteCount(market.getFavoriteCount())
            .reviewCount(0)
            .build();

    private SimpleMarket anotherSimpleMarket = SimpleMarket.builder()
            .marketId(2L)
            .description(anotherMarket.getDescription())
            .eventCount(0)
            .favoriteCount(anotherMarket.getFavoriteCount())
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

        //When
        marketService.createMarket(createRequest, user.getId());

        //Then
        verify(userRepository).findById(any());
        verify(marketConverter).convertToMarket(createRequest, user);
        verify(marketRepository).save(newMarket);
    }

    @Test
    void getMarketsByUser(){
        //Given
        Page<Market> markets = new PageImpl<>(List.of(market, anotherMarket));

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(marketRepository.findByUser(user, pageable)).thenReturn(markets);
        when(marketConverter.convertToSimpleMarket(market, 0, 0)).thenReturn(simpleMarket);
        when(marketConverter.convertToSimpleMarket(anotherMarket, 0, 0)).thenReturn(anotherSimpleMarket);

        //When
        marketService.getMarketsByUser(user.getId(), pageable);

        //Then
        verify(marketRepository).findByUser(user, pageable);
        verify(marketConverter).convertToSimpleMarket(market, 0, 0);
        verify(marketConverter).convertToSimpleMarket(anotherMarket, 0, 0);
        verify(marketConverter).convertToMarketReadResponse(any());
    }
}
