package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.dto.response.DetailMarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.market.dto.request.MarketCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.dto.response.MyMarketReadResponse;
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
    public MyMarketReadResponse getMarketsByUser(User user) {
        Market market = marketRepository.findByUser(user)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, user.getId()));

        return marketConverter.convertToSimpleMarket(market);
    }

    @Transactional(readOnly = true)
    public DetailMarketReadResponse getMarketById(Long id) {
        Market market = marketRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.MARKET_NOT_FOUNDED, id));

        return marketConverter.convertToDetailMarketReadResponse(market);
    }

    @Transactional
    public Long createMarket(MarketCreateRequest createRequest, User user) {
        userService.changeAuthorityToBusiness(user.getEmail());

        Market market = marketConverter.convertToMarket(createRequest, user);
        return marketRepository.save(market).getId();
    }

}
