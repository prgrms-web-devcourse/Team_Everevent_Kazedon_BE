package kdt.prgrms.kazedon.everevent.domain.review.repository;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.review.Review;
import kdt.prgrms.kazedon.everevent.domain.review.dto.UserReview;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
  Page<Review> findByEvent(Event event, Pageable pageable);

  @Query("SELECT new kdt.prgrms.kazedon.everevent.domain.review.dto.UserReview(r.id, r.description, m.name, r.pictureUrl) " +
          "FROM Review r " +
          "LEFT JOIN Event e ON e.id = r.event.id " +
          "LEFT JOIN Market m ON m.id = e.market.id " +
          "WHERE r.user.id = :userId")
  Page<UserReview> findByUser(@Param("userId") Long userId, Pageable pageable);

  long countByUser(User user);
}
