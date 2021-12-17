package kdt.prgrms.kazedon.everevent.domain.like.repository;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.like.EventLike;
import kdt.prgrms.kazedon.everevent.domain.like.dto.SimpleEventLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLikeRepository extends JpaRepository<EventLike, Long> {

  boolean existsEventLikeByUserIdAndEventId(Long userId, Long eventId);

  Optional<EventLike> findByUserIdAndEventId(Long userId, Long eventId);

  @Query("select "
      + "new kdt.prgrms.kazedon.everevent.domain.like.dto.SimpleEventLike(e.id, e.expiredAt, e.name, e.market.name, e.likeCount, e.reviewCount) "
      + "from EventLike el join el.event e on el.user.id = :userId")
  Page<SimpleEventLike> findSimpleLikeByUserId(Long userId, Pageable pageable);

}
