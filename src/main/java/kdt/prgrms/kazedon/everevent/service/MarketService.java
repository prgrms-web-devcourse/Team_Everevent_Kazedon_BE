package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.dto.DetailMarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.market.dto.SimpleMarket;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.MarketConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;
    private final MarketConverter marketConverter;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public MarketReadResponse getMarketsByUser(Long userId, Pageable pageable) {
        int eventCount = 0;
        int reviewCount = 0;

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, userId));

        Page<SimpleMarket> simpleMarkets = marketRepository
                .findByUser(user, pageable)
                .map(market -> marketConverter.convertToSimpleMarket(market, eventCount, reviewCount));

        return marketConverter.convertToMarketReadResponse(simpleMarkets);
    }

    @Transactional(readOnly = true)
    public DetailMarketReadResponse getMarketById(Long id) {
        Market market = marketRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, id));

        return marketConverter.convertToDetailMarketReadResponse(market);
    }

    @Transactional
    public Long createMarket(MarketCreateRequest createRequest, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, userId));

        userService.changeAuthorityToBusiness(user.getEmail());

        Market market = marketConverter.convertToMarket(createRequest, user);
        return marketRepository.save(market).getId();
    }

}
