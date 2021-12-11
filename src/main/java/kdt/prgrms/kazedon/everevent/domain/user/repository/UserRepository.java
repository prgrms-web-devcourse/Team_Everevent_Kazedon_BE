package kdt.prgrms.kazedon.everevent.domain.user.repository;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  @Query("select (count(u) > 0) from User u where u.email = :email")
  boolean existsByEmail(String email);

  @Query("select (count(u) > 0) from User u where u.nickname = :nickname")
  boolean existsByNickname(String nickname);
}
