package kdt.prgrms.kazedon.everevent.domain.like.repository;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.like.EventLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLikeRepository extends JpaRepository<EventLike, Long> {
  boolean existsEventLikeByUserIdAndEventId(Long userId, Long eventId);

  Optional<EventLike> findByUserIdAndEventId(Long userId, Long eventId);

}
