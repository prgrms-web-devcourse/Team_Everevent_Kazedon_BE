package kdt.prgrms.kazedon.everevent.domain.review.repository;

import kdt.prgrms.kazedon.everevent.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
