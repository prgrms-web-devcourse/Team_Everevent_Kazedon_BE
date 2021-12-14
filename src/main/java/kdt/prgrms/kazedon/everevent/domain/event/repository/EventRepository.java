package kdt.prgrms.kazedon.everevent.domain.event.repository;

import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT e FROM Event e INNER JOIN e.market m WHERE m.address LIKE %:location%")
    Page<Event> findByLocation(@Param("location") String location, Pageable pageable);

    Page<Event> findByMarket(Market market, Pageable pageable);
}
