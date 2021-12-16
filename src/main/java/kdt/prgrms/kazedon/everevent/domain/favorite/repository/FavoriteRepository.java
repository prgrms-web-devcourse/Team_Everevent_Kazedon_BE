package kdt.prgrms.kazedon.everevent.domain.favorite.repository;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.favorite.Favorite;
import kdt.prgrms.kazedon.everevent.domain.favorite.dto.SimpleFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

  Optional<Favorite> findByUserIdAndMarketId(Long userId, Long marketId);

  boolean existsFavoriteByUserIdAndMarketId(Long userId, Long marketId);

  @Query("select "
      + "new kdt.prgrms.kazedon.everevent.domain.favorite.dto.SimpleFavorite(m.id, m.name, m.favoriteCount) "
      + "from Favorite f join f.market m on f.user.id = :memberId")
  Page<SimpleFavorite> findSimpleFavoriteByUserId(Long memberId, Pageable pageable);
}
