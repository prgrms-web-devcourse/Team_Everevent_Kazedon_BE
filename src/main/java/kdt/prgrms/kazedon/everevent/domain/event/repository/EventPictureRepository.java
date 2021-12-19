package kdt.prgrms.kazedon.everevent.domain.event.repository;

import java.util.List;
import kdt.prgrms.kazedon.everevent.domain.event.EventPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventPictureRepository extends JpaRepository<EventPicture, Long> {

  @Query("select e.url from EventPicture e where e.event.id = :eventId")
  List<String> findEventPictureUrlsByEventId(Long eventId);
}
