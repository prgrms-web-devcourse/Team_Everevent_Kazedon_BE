package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MyMarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.MarketConverter;
import lombok.RequiredArgsConstructor;
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
    public MyMarketReadResponse getMarketsByUser(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, userId));

        Market market = marketRepository.findByUser(user)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, userId));

        return marketConverter.convertToSimpleMarket(market);
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
