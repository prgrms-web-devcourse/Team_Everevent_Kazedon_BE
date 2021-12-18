package kdt.prgrms.kazedon.everevent.domain.event.repository;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.dto.DetailEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEvent;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT "
        + "new kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEvent"
        + "(e.id, e.name, e.expiredAt, e.market.name, MIN(ep.url), e.likeCount, e.reviewCount"
        + ", CASE WHEN el.id IS null THEN false ELSE true END"
        + ", e.maxParticipants - e.participantCount) "
        + "FROM Event e "
        + "LEFT JOIN EventPicture ep ON e.id = ep.event.id "
        + "LEFT JOIN EventLike el ON (e.id = el.event.id AND el.user.id = :userId) "
        + "WHERE e.market.address LIKE %:location% "
        + "GROUP BY e.id")
    Page<SimpleEvent> findByLocation(@Param("location") String location,
        @Param("userId") Long userId, Pageable pageable);

    @Query("select "
        + "new kdt.prgrms.kazedon.everevent.domain.event.dto.DetailEvent "
        + "(e.name, e.expiredAt, e.market.name, e.market.description, e.description, "
        + "CASE WHEN el.id is null THEN false ELSE true END, "
        + "CASE WHEN ue.id is null THEN false ELSE ue.isCompleted END, "
        + "CASE WHEN f.id is null THEN false ELSE true END) "
        + "from Event e "
        + "left outer join EventLike el on (e.id = el.event.id and el.user.id = :userId) "
        + "left outer join UserEvent ue on (e.id = ue.event.id and ue.user.id = :userId) "
        + "left outer join Favorite f on (e.market.id = f.market.id and f.user.id = :userId) "
        + "where e.id= :eventId"
    )
    Optional<DetailEvent> findDetailEventById(@Param("eventId") Long eventId,
        @Param("userId") Long userId);

    Page<Event> findByMarket(Market market, Pageable pageable);
}
