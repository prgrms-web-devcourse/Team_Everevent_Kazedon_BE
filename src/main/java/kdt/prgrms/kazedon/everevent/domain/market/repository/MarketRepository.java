package kdt.prgrms.kazedon.everevent.domain.market.repository;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long> {
    Optional<Market> findByUser(User user);

    @Query("SELECT CASE WHEN COUNT(m)> 0 THEN true ELSE false END FROM Market m "
        + "WHERE m.user.id = :userId AND m.id = :marketId")
    boolean isPossibleToCreateEvent(Long marketId, Long userId);

    boolean existsByUser(User user);
}
