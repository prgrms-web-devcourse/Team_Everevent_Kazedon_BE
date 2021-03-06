package kdt.prgrms.kazedon.everevent.domain.event.repository;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.DetailEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.response.SimpleEvent;
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
        + "new kdt.prgrms.kazedon.everevent.domain.event.dto.response.SimpleEvent"
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

    @Query("SELECT "
        + "new kdt.prgrms.kazedon.everevent.domain.event.dto.response.DetailEvent "
        + "(e.name, e.expiredAt, e.market.id, e.market.name, e.market.description, e.description, e.maxParticipants, (e.maxParticipants - e.participantCount), "
        + "CASE WHEN el.id is null THEN false ELSE true END, "
        + "CASE WHEN ue.id is null THEN 'notParticipated' "
        + "WHEN ue.isCompleted is false THEN 'participated' "
        + "ELSE 'completed' END, "
        + "CASE WHEN f.id is null THEN false ELSE true END) "
        + "FROM Event e "
        + "LEFT OUTER JOIN EventLike el ON (e.id = el.event.id AND el.user.id = :userId) "
        + "LEFT OUTER JOIN UserEvent ue ON (e.id = ue.event.id AND ue.user.id = :userId) "
        + "LEFT OUTER JOIN Favorite f ON (e.market.id = f.market.id AND f.user.id = :userId) "
        + "WHERE e.id= :eventId"
    )
    Optional<DetailEvent> findDetailEventById(@Param("eventId") Long eventId,
        @Param("userId") Long userId);

    @Query("SELECT "
            + "new kdt.prgrms.kazedon.everevent.domain.event.dto.response.SimpleEvent"
            + "(e.id, e.name, e.expiredAt, e.market.name, MIN(ep.url), e.likeCount, e.reviewCount"
            + ", false, e.maxParticipants - e.participantCount) "
            + "FROM Event e "
            + "LEFT JOIN EventPicture ep ON e.id = ep.event.id "
            + "WHERE e.market.address LIKE %:location% "
            + "GROUP BY e.id")
    Page<SimpleEvent> findByLocation(@Param("location") String location, Pageable pageable);
    
    Page<Event> findByMarket(Market market, Pageable pageable);
}
