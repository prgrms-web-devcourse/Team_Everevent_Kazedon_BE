package kdt.prgrms.kazedon.everevent.controller;

import java.net.URI;
import javax.validation.Valid;
import kdt.prgrms.kazedon.everevent.configures.auth.AuthUser;
import kdt.prgrms.kazedon.everevent.domain.market.dto.request.MarketUpdateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.dto.response.DetailMarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.market.dto.request.MarketCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.dto.response.MyMarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class MarketController {

    private final MarketService marketService;

    @GetMapping("markets")
    public ResponseEntity<MyMarketReadResponse> getMarkets(@AuthUser User user) {
        return ResponseEntity.ok(marketService.getMarketsByUser(user));
    }

    @GetMapping("markets/{marketId}")
    public ResponseEntity<DetailMarketReadResponse> getMarket(@PathVariable Long marketId) {
        return ResponseEntity.ok(marketService.getMarketById(marketId));
    }

    @PostMapping("markets")
    public ResponseEntity<Void> createMarket(@Valid @RequestBody MarketCreateRequest marketCreateRequest,
        @AuthUser User user) {
        marketService.createMarket(marketCreateRequest, user);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .build()
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("markets/{marketId}")
    public ResponseEntity<Void> updateMarket(@AuthUser User user,
                                             @PathVariable Long marketId,
                                             @Valid @RequestBody MarketUpdateRequest updateRequest){
        marketService.updateMarket(user, marketId, updateRequest);
        return ResponseEntity.noContent().build();
    }
}
