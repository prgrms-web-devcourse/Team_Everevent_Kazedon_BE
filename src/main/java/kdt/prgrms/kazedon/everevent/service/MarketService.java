package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
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
@Transactional
public class MarketService {
    private final MarketRepository marketRepository;
    private final MarketConverter marketConverter;
    private final UserRepository userRepository;

    public MarketReadResponse getMarketsByUser(Long userId, Pageable pageable){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, userId));

        Page<SimpleMarket> simpleMarkets = marketRepository
                .findByUser(user, pageable)
                .map(market -> marketConverter.convertToSimpleMarket(market, 0, 0));

        return marketConverter.convertToMarketReadResponse(simpleMarkets);
    }

    public Long createMarket(MarketCreateRequest createRequest, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, userId));

        Market market = marketConverter.convertToMarket(createRequest, user);
        return marketRepository.save(market).getId();
    }
}
