package kdt.prgrms.kazedon.everevent.domain.like.repository;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.like.EventLike;
import kdt.prgrms.kazedon.everevent.domain.like.dto.response.SimpleEventLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLikeRepository extends JpaRepository<EventLike, Long> {

  boolean existsEventLikeByUserIdAndEventId(Long userId, Long eventId);

  Optional<EventLike> findByUserIdAndEventId(Long userId, Long eventId);

  @Query(
      "SELECT new kdt.prgrms.kazedon.everevent.domain.like.dto.response.SimpleEventLike(e.id, e.expiredAt, e.name, e.market.name, e.likeCount, e.reviewCount) "
          + "FROM EventLike el "
          + "JOIN el.event e ON el.user.id = :userId"
  )
  Page<SimpleEventLike> findSimpleLikeByUserId(@Param("userId") Long userId, Pageable pageable);

}
