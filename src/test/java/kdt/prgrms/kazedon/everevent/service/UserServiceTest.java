package kdt.prgrms.kazedon.everevent.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.UserType;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.repository.AuthorityRepository;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.UserConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private AuthorityRepository authorityRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserConverter userConverter;

  private SignUpRequest signUpRequest;

  private User user;

  private String userEmail;

  @BeforeEach
  public void setUp() {
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
    given(passwordEncoder.encode(any())).willReturn("password");
    ReflectionTestUtils.setField(user2, "id", 2L);
    given(userRepository.findByEmail(signupEmail)).willReturn(Optional.of(user2));

    //When
    userService.signUp(signUpRequest2);
    Optional<User> findUser = userRepository.findByEmail(signupEmail);

    //Then
    assertThat(findUser.get(),allOf(notNullValue(),samePropertyValuesAs(user2)));
  }

  @Test
  void getUser() {
    //Given
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userConverter.convertToUserReadResponse(user)).thenReturn(any());

    //When
    userService.getUser(userId);

    //Then
    verify(userRepository).findById(userId);
    verify(userConverter).convertToUserReadResponse(user);
  }

  @Test
  void getNonExistingUser(){
    //Given
    Long invalidUserId = Long.MAX_VALUE;
    when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

    //When
    //Then
    assertThrows(NotFoundException.class, () -> userService.getUser(invalidUserId));
    verify(userRepository).findById(invalidUserId);
  }

  @Test
  void changeAuthorityToBusinessTest() {
    //Given
    given(userRepository.findByEmail(userEmail)).willReturn(Optional.of(user));
    User business = new User(signUpRequest);
    business.changeAuthority(UserType.ROLE_BUSINESS);
    given(userRepository.save(any())).willReturn(business);

    //When
    UserType userType = userService.changeAuthorityToBusiness(userEmail);

    //Then
    assertThat(userType, is(UserType.ROLE_BUSINESS));

  }
}
