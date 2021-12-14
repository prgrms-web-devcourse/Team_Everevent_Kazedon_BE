package kdt.prgrms.kazedon.everevent.domain.user.repository;

import kdt.prgrms.kazedon.everevent.domain.user.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

  void deleteByUserId(Long userId);

}
