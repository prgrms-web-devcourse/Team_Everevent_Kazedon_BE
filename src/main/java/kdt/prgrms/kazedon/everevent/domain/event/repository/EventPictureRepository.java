package kdt.prgrms.kazedon.everevent.domain.event.repository;

import kdt.prgrms.kazedon.everevent.domain.event.EventPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventPictureRepository extends JpaRepository<EventPicture, Long> {
}
