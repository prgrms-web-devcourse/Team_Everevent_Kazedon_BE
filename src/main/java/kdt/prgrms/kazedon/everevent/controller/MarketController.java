package kdt.prgrms.kazedon.everevent.controller;

import kdt.prgrms.kazedon.everevent.configures.auth.AuthUser;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class MarketController {
    private final MarketService marketService;

    @GetMapping("markets")
    public ResponseEntity<MarketReadResponse> getMarkets(@AuthUser User user,
                                                         @PageableDefault(size=20, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(marketService.getMarketsByUser(user.getId(), pageable));
    }

    @PostMapping("markets")
    public ResponseEntity createMarket(@Valid @RequestBody MarketCreateRequest marketCreateRequest,
                                       @AuthUser User user){
        marketService.createMarket(marketCreateRequest, user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
