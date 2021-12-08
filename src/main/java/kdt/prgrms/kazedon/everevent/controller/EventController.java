package kdt.prgrms.kazedon.everevent.controller;

import kdt.prgrms.kazedon.everevent.domain.event.dto.DetailEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEventReadResponse;
import kdt.prgrms.kazedon.everevent.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class EventController {
    private final EventService eventService;

    @GetMapping("events")
    public ResponseEntity<SimpleEventReadResponse> getEvents(@RequestParam String location,
                                                             @PageableDefault(size=20, sort="expiredAt", direction = Sort.Direction.DESC) Pageable pageable){
        return new ResponseEntity<>(
                eventService.getEventsByLocation(location, pageable),
                HttpStatus.OK
        );
    }

    @GetMapping("events/{event_id}")
    public ResponseEntity<DetailEventReadResponse> getEvent(@PathVariable("event_id") Long eventId){
        return new ResponseEntity<>(
                eventService.getEventById(eventId),
                HttpStatus.OK
        );
    }
}
