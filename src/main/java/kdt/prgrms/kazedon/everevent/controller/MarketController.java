package kdt.prgrms.kazedon.everevent.controller;

import java.net.URI;
import javax.validation.Valid;
import kdt.prgrms.kazedon.everevent.configures.auth.AuthUser;
import kdt.prgrms.kazedon.everevent.domain.market.dto.DetailMarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MyMarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.MarketService;
import kdt.prgrms.kazedon.everevent.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class MarketController {

    private final MarketService marketService;

    private final UserService userService;

    @GetMapping("markets")
    public ResponseEntity<MyMarketReadResponse> getMarkets(@AuthUser User user) {
        return ResponseEntity.ok(marketService.getMarketsByUser(user.getId()));
    }

    @GetMapping("markets/{marketId}")
    public ResponseEntity<DetailMarketReadResponse> getMarket(@PathVariable Long marketId) {
        return ResponseEntity.ok(marketService.getMarketById(marketId));
    }

    @PostMapping("markets")
    public ResponseEntity<Void> createMarket(@Valid @RequestBody MarketCreateRequest marketCreateRequest,
        @AuthUser User user) {
        marketService.createMarket(marketCreateRequest, user.getId());

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .build()
            .toUri();

        return ResponseEntity.created(location).build();
    }
}
