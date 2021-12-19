package kdt.prgrms.kazedon.everevent.controller;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kdt.prgrms.kazedon.everevent.configures.auth.AuthUser;
import kdt.prgrms.kazedon.everevent.domain.event.dto.DetailEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.EventCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.event.dto.EventUpdateRequest;
import kdt.prgrms.kazedon.everevent.domain.event.dto.MarketEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.EventService;
import kdt.prgrms.kazedon.everevent.service.UserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class EventController {

    private final EventService eventService;
    private final UserEventService userEventService;

    @GetMapping("events")
    public ResponseEntity<SimpleEventReadResponse> getEvents(@RequestParam String location,
                                                             @AuthUser User user,
                                                             @PageableDefault(size=20, sort="expiredAt", direction = Sort.Direction.DESC) Pageable pageable){
        if(user == null){
            return ResponseEntity.ok(eventService.getEventsByLocation(location, pageable));
        }

        return ResponseEntity.ok(eventService.getEventsByLocation(location, user.getEmail(), pageable));
    }

    @GetMapping("events/{eventId}")
    public ResponseEntity<DetailEventReadResponse> getEvent(@PathVariable("eventId") Long eventId,
        @AuthUser User user) {
        return ResponseEntity.ok(eventService.getEventById(eventId, user.getEmail()));
    }

    @PostMapping(path = "events", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createEvent(@Valid @RequestPart EventCreateRequest request,
                                            @RequestPart(required = false) List<MultipartFile> files,
                                            @AuthUser User user){
        Long eventId = eventService.createEvent(request, files);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{eventId}")
                .buildAndExpand(eventId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("events/{eventId}/participants")
    public ResponseEntity<Void> participateEventByUser(@PathVariable Long eventId, @AuthUser User user) {
        userEventService.participateEventByUser(user.getId(), eventId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("events/{eventId}/participants")
    public ResponseEntity<Void> completeEventByBusiness(@PathVariable Long eventId, @AuthUser User user) {
        userEventService.completeEventByBusiness(user.getId(), eventId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("events/{eventId}")
    public ResponseEntity<Void> updateEvent(@PathVariable Long eventId,
                                            @RequestBody EventUpdateRequest eventUpdateRequest,
                                            @AuthUser User user) {
        eventService.update(eventId, user.getId(), eventUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/markets/{marketId}/events")
    public ResponseEntity<MarketEventReadResponse> getEvents(@PathVariable Long marketId,
                                                             @PageableDefault(size=20, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(eventService.getEventsByMarket(marketId, pageable));
    }
}
