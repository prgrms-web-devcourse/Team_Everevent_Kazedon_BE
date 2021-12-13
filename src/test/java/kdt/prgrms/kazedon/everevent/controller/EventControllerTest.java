package kdt.prgrms.kazedon.everevent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kdt.prgrms.kazedon.everevent.EvereventApplication;
import kdt.prgrms.kazedon.everevent.configures.JwtAuthenticationProvider;
import kdt.prgrms.kazedon.everevent.configures.auth.CustomUserDetails;
import kdt.prgrms.kazedon.everevent.domain.event.dto.DetailEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.event.dto.EventCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.event.dto.SimpleEventReadResponse;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.CustomUserDetailService;
import kdt.prgrms.kazedon.everevent.service.EventService;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = EvereventApplication.class)
class EventControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private Pageable pageable;

    @MockBean
    private SimpleEventReadResponse simpleEventReadResponse;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    private SignUpRequest signUpRequest = SignUpRequest.builder()
            .email("user-email@gmail.com")
            .nickname("user-nickname")
            .password("$2b$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G") //password
            .build();

    private User user = new User(signUpRequest);

    private String token = jwtAuthenticationProvider().createToken(user.getEmail(), List.of("ROLE_USER"));

    private Market market = Market.builder()
            .name("test-market")
            .address("test-address")
            .description("test-description")
            .user(user)
            .build();

    private DetailEventReadResponse detailEventReadResponse = DetailEventReadResponse.builder()
            .eventName("test-event")
            .build();

    private EventCreateRequest createRequest = EventCreateRequest.builder()
            .marketId(1L)
            .description("test-event-description")
            .expiredAt(LocalDateTime.now().plusDays(3))
            .maxParticipants(10)
            .name("test-event")
            .build();

    private EventCreateRequest invalidCreateRequest = EventCreateRequest.builder()
            .marketId(Long.MAX_VALUE)
            .description("test-event-description")
            .expiredAt(LocalDateTime.now().plusDays(3))
            .maxParticipants(-1000)
            .name("test-event")
            .build();

    private JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(customUserDetailService);
    }

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    void getEventsByLocation() throws Exception {
        //Given
        String location = "test-location";
        when(eventService.getEventsByLocation(location, pageable)).thenReturn(simpleEventReadResponse);

        //When
        //Then
        mockMvc.perform(get("/api/v1/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("location", location))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getEventById() throws Exception {
        //Given
        Long eventId = 1L;
        when(eventService.getEventById(eventId)).thenReturn(detailEventReadResponse);

        //When
        //Then
        mockMvc.perform(get("/api/v1/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getEventByInvalidId() throws Exception {
        //Given
        Long eventId = 10L;
        when(eventService.getEventById(eventId)).thenThrow(NotFoundException.class);

        //When
        //Then
        mockMvc.perform(get("/api/v1/events/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void createEvent() throws Exception {
        //Given
        Long eventId = 1L;
        when(customUserDetailService.loadUserByUsername(user.getEmail())).thenReturn(new CustomUserDetails(user));
        when(eventService.createEvent(createRequest)).thenReturn(eventId);

        //When
        //Then
        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
                        .header("X-AUTH-TOKEN", token))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void createEventUsingInvalidData() throws Exception {
        //Given
        Long eventId = 1L;
        when(customUserDetailService.loadUserByUsername(user.getEmail())).thenReturn(new CustomUserDetails(user));
        when(eventService.createEvent(invalidCreateRequest)).thenReturn(eventId);

        //When
        //Then
        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateRequest))
                        .header("X-AUTH-TOKEN", token))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}