package kdt.prgrms.kazedon.everevent.ec2;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.Authority;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.UserType;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DumpDataConfiguration implements CommandLineRunner {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final MarketRepository marketRepository;

    @Override
    public void run(String... args) throws Exception {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test-password")
                .nickname("test-nickname")
                .location("test-user-location")
                .build();

        user.changeAuthority(UserType.ROLE_ADMIN);
        userRepository.save(user);

        Market market = Market.builder()
                .user(user)
                .name("test-market-name")
                .description("test-market-description")
                .address("test-market-adderess")
                .build();

        marketRepository.save(market);

        eventRepository.save(Event.builder()
                .market(market)
                .name("test-event")
                .expiredAt(LocalDateTime.now().plusDays(3))
                .description("this is a test event. nice to meet you!")
                .maxParticipants(5)
                .build()
        );

        eventRepository.save(Event.builder()
                .market(market)
                .name("test-another-event")
                .expiredAt(LocalDateTime.now().plusDays(3))
                .description("this is a test event. nice to meet you:)")
                .maxParticipants(3)
                .build()
        );
    }
}
