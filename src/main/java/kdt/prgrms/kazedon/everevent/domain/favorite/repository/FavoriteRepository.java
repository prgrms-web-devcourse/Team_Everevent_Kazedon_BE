package kdt.prgrms.kazedon.everevent.domain.favorite.repository;

import java.util.List;
import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.favorite.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

  @Query("select f from Favorite f where f.user.id = :userId and f.market.id = :marketId")
  Optional<Favorite> findByUserAndMarket(Long userId, Long marketId);

  @Query("select (count(f) > 0) from Favorite f where f.user.id = :userId and f.market.id = :marketId")
  boolean existsFavoriteByUserIdAndMarketId(Long userId, Long marketId);

}
