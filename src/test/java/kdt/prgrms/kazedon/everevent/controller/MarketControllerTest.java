package kdt.prgrms.kazedon.everevent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kdt.prgrms.kazedon.everevent.EvereventApplication;
import kdt.prgrms.kazedon.everevent.configures.JwtAuthenticationProvider;
import kdt.prgrms.kazedon.everevent.configures.auth.CustomUserDetails;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketCreateRequest;
import kdt.prgrms.kazedon.everevent.domain.market.dto.MarketReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.service.CustomUserDetailService;
import kdt.prgrms.kazedon.everevent.service.MarketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = EvereventApplication.class)
public class MarketControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private MarketService marketService;

    @MockBean
    private Pageable pageable;

    @MockBean
    private MarketReadResponse readResponse;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    private SignUpRequest signUpRequest = SignUpRequest.builder()
            .email("user-email")
            .nickname("user-nickname")
            .password("$2b$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G") //password
            .build();

    private User user = new User(signUpRequest);

    private String token = jwtAuthenticationProvider().createToken(user.getEmail(), List.of("ROLE_USER"));

    private MarketCreateRequest invalidMarketCreateRequest = MarketCreateRequest.builder()
            .name("name exceed 50 length ----------------------------------------------------------- ")
            .address("market-address")
            .description("market-description")
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
    void getMarketsByUser() throws Exception {
        //Given
        when(customUserDetailService.loadUserByUsername(user.getEmail())).thenReturn(new CustomUserDetails(user));
        when(marketService.getMarketsByUser(user.getId(), pageable)).thenReturn(readResponse);

        //When
        //Then
        mockMvc.perform(get("/api/v1/markets")
                        .contentType(MediaType.APPLICATION_JSON).header("X-AUTH-TOKEN", token))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void createMarket() throws Exception {
        //Given
        MarketCreateRequest marketCreateRequest = MarketCreateRequest.builder()
                .name("market-name")
                .address("market-address@gmail.com")
                .description("market-description")
                .build();

        when(customUserDetailService.loadUserByUsername(user.getEmail())).thenReturn(new CustomUserDetails(user));
        when(marketService.createMarket(marketCreateRequest, user.getId())).thenReturn(1L);

        //When
        //Then
        mockMvc.perform(post("/api/v1/markets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(marketCreateRequest))
                        .header("X-AUTH-TOKEN", token))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void createMarketUsingInvalidData() throws Exception {
        //Given
        when(customUserDetailService.loadUserByUsername(user.getEmail())).thenReturn(new CustomUserDetails(user));
        when(marketService.createMarket(invalidMarketCreateRequest, user.getId())).thenReturn(1L);

        //When
        //Then
        mockMvc.perform(post("/api/v1/markets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMarketCreateRequest))
                        .header("X-AUTH-TOKEN", token))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
