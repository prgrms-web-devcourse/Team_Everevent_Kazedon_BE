package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEvent;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.service.converter.EventConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
    private final EventRepository eventRepository;
    private final EventConverter eventConverter;

    public SimpleEventReadResponse getEventsByLocation(String location, Pageable pageable){
        Page<SimpleEvent> simpleEvents = eventRepository
                .findByLocation(location, pageable)
                .map(event -> eventConverter.convertToSimpleEvent(event, false));
        //      .filter(condition checking if the user likes this event using [event_like] table)

        return eventConverter.convertToSimpleEventReadResponse(simpleEvents);
    }
}
