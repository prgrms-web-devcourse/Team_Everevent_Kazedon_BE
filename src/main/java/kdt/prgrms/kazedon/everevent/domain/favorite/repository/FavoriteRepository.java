package kdt.prgrms.kazedon.everevent.domain.favorite.repository;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.favorite.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

  Optional<Favorite> findByUserIdAndMarketId(Long userId, Long marketId);

  boolean existsFavoriteByUserIdAndMarketId(Long userId, Long marketId);

}
