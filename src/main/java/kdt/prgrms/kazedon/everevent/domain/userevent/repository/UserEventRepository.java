package kdt.prgrms.kazedon.everevent.domain.userevent.repository;

import java.util.List;
import java.util.Optional;

import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.userevent.UserEvent;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEventRepository extends JpaRepository<UserEvent, Long> {

  boolean existsUserEventsByUserIdAndEventId(Long userId, Long marketId);

  Optional<UserEvent> findByUserIdAndEventId(Long userId, Long marketId);

  @EntityGraph(attributePaths = "event")
  List<UserEvent> findAllByUserId(Long userId);

  long countByUser(User user);
}
