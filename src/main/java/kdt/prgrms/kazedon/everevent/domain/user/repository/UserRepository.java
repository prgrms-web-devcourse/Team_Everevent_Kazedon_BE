package kdt.prgrms.kazedon.everevent.domain.user.repository;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findUserByNickname(String nickname);
}
