package kdt.prgrms.kazedon.everevent.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SimpleUserResponse;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

  @InjectMocks
  private CustomUserDetailService userDetailService;

  @Mock
  private UserRepository userRepository;

  private SignUpRequest signUpRequest;

  private User user;

  private String userEmail;

  @BeforeEach
  public void setUp(){
    userEmail = "test-user@gmail.com";
    signUpRequest = SignUpRequest.builder()
        .email(userEmail)
        .nickname("user-nickname")
        .password("$2b$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G") //password
        .build();
    user = new User(signUpRequest);
    userRepository.save(user);
    ReflectionTestUtils.setField(user, "id", 1L);
  }

  @Test
  void signUp() {
    //Given
    String signupEmail = "test-user2@gmail.com";
    SignUpRequest signUpRequest2 = SignUpRequest.builder()
        .email(signupEmail)
        .nickname("user-nickname2")
        .password("$2b$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G") //password
        .build();
    User user2 = new User(signUpRequest2);

    given(userRepository.save(any())).willReturn(user2);
    ReflectionTestUtils.setField(user2, "id", 2L);
    given(userRepository.findByEmail(signupEmail)).willReturn(Optional.of(user2));

    //When
    userDetailService.signUp(signUpRequest2);
    Optional<User> findUser = userRepository.findByEmail(signupEmail);

    //Then
    assertThat(findUser.get(),allOf(notNullValue(),samePropertyValuesAs(user2)));
  }

  @Test
  void findByEmail() {
    //Given
    given(userRepository.findByEmail(userEmail)).willReturn(Optional.of(user));

    //When
    SimpleUserResponse findUserResponse = userDetailService.findByEmail(userEmail);

    //Then
    assertThat(findUserResponse, allOf(notNullValue(),samePropertyValuesAs(new SimpleUserResponse(user))));

  }
}