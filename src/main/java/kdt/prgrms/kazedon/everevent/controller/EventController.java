package kdt.prgrms.kazedon.everevent.controller;

import java.util.List;
import kdt.prgrms.kazedon.everevent.configures.auth.AuthUser;
import kdt.prgrms.kazedon.everevent.domain.event.dto.DetailEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.EventCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class EventController {
    private final EventService eventService;

    @GetMapping("events")
    public ResponseEntity<SimpleEventReadResponse> getEvents(@RequestParam String location,
                                                             @PageableDefault(size=20, sort="expiredAt", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(eventService.getEventsByLocation(location, pageable));
    }

    @GetMapping("events/{eventId}")
    public ResponseEntity<DetailEventReadResponse> getEvent(@PathVariable("eventId") Long eventId){
        return ResponseEntity.ok(eventService.getEventById(eventId));
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
}
