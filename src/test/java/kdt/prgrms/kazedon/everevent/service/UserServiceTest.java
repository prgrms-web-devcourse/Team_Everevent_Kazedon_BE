package kdt.prgrms.kazedon.everevent.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.UserType;
import kdt.prgrms.kazedon.everevent.domain.user.dto.request.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.request.UserUpdateRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.response.UserInfoResponse;
import kdt.prgrms.kazedon.everevent.domain.user.dto.response.UserReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.DuplicateUserArgumentException;
import kdt.prgrms.kazedon.everevent.exception.InvalidPasswordException;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.UserConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserConverter userConverter;

  @Mock
  private AuthenticationManager authenticationManager;

  private String userEmail = "test-user@gmail.com";

  private SignUpRequest signUpRequest = SignUpRequest.builder()
      .email("test-user@gmail.com")
      .nickname("user-nickname")
      .password("password")
      .build();

  private String encodedPassword = "$2b$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G";

  private User user = User.builder()
      .email(signUpRequest.getEmail())
      .password(encodedPassword)
      .nickname(signUpRequest.getNickname())
      .location("")
      .build();

  @Test
  void signUpSuccessTest() {
    //given
    String signupEmail = "test-user2@gmail.com";
    String encodedPassword2 = "$2b$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G";
    String nickname = "user-nickname2";

    SignUpRequest signUpRequest2 = SignUpRequest.builder()
        .email(signupEmail)
        .nickname(nickname)
        .password("new-password")
        .build();

    User user2 = User.builder()
        .email(signUpRequest2.getEmail())
        .password(encodedPassword2)
        .nickname(signUpRequest2.getNickname())
        .location("")
        .build();

    ReflectionTestUtils.setField(user2, "id", 2L);

    when(userRepository.existsByEmail(signupEmail)).thenReturn(false);
    when(userRepository.existsByNickname(nickname)).thenReturn(false);
    when(passwordEncoder.encode(signUpRequest2.getPassword())).thenReturn(encodedPassword2);
    when(userConverter.convertToUser(signUpRequest2, encodedPassword2)).thenReturn(user2);
    when(userRepository.save(user2)).thenReturn(user2);

    //when
    userService.signUp(signUpRequest2);

    //then
    verify(userRepository).existsByEmail(signupEmail);
    verify(userRepository).existsByNickname(nickname);
    verify(passwordEncoder).encode(signUpRequest2.getPassword());
    verify(userConverter).convertToUser(signUpRequest2, encodedPassword2);
    verify(userRepository).save(user2);
  }

  @Test
  void signUpEmailDuplicateTest() {
    //given
    String signupEmail = "test-user@gmail.com";
    SignUpRequest signUpRequest2 = SignUpRequest.builder()
        .email(signupEmail)
        .nickname("user-nickname2")
        .password("new-password")
        .build();

    when(userRepository.existsByEmail(signupEmail)).thenReturn(true);

    //when
    assertThrows(DuplicateUserArgumentException.class, () -> userService.signUp(signUpRequest2));

    //then
    verify(userRepository).existsByEmail(signupEmail);
  }

  @Test
  void signUpNicknameDuplicateTest() {
    //given
    String signupEmail = "test-user2@gmail.com";
    String nickname = "user-nickname";

    SignUpRequest signUpRequest2 = SignUpRequest.builder()
        .email(signupEmail)
        .nickname(nickname)
        .password("new-password")
        .build();

    when(userRepository.existsByEmail(signupEmail)).thenReturn(false);
    when(userRepository.existsByNickname(nickname)).thenReturn(true);

    //when
    assertThrows(DuplicateUserArgumentException.class, () -> userService.signUp(signUpRequest2));

    //then
    verify(userRepository).existsByEmail(signupEmail);
    verify(userRepository).existsByNickname(nickname);
  }


  @Test
  void changeAuthorityToBusinessSuccessTest() {
    //given
    List<UserType> roles = new ArrayList<>();
    roles.add(UserType.ROLE_USER);
    roles.add(UserType.ROLE_BUSINESS);

    User business = User.builder()
        .email(signUpRequest.getEmail())
        .password(encodedPassword)
        .nickname(signUpRequest.getNickname())
        .location("")
        .build();
    business.addAuthority(UserType.ROLE_BUSINESS);
    ReflectionTestUtils.setField(business, "id", 1L);

    when(userRepository.findByEmail(business.getEmail())).thenReturn(Optional.of(business));
    when(userRepository.save(business)).thenReturn(business);

    //when
    userService.changeAuthorityToBusiness(signUpRequest.getEmail());

    //then
    verify(userRepository).findByEmail(business.getEmail());
    verify(userRepository).save(business);
  }

  @Test
  void changeAuthorityInvalidUserTest() {
    //given
    List<UserType> roles = new ArrayList<>();
    roles.add(UserType.ROLE_USER);
    roles.add(UserType.ROLE_BUSINESS);

    String nonUserEmail = "invalid@gmail.com";
    User business = User.builder()
        .email(nonUserEmail)
        .password(encodedPassword)
        .nickname(signUpRequest.getNickname())
        .location("")
        .build();
    business.addAuthority(UserType.ROLE_BUSINESS);
    ReflectionTestUtils.setField(business, "id", 1L);

    when(userRepository.findByEmail(nonUserEmail)).thenReturn(Optional.empty());

    //when
    assertThrows(NotFoundException.class,
        () -> userService.changeAuthorityToBusiness(nonUserEmail));

    //then
    verify(userRepository).findByEmail(nonUserEmail);
  }

  @Test
  void updateUserNicknameSuccessTest() {
    //given
    UserUpdateRequest updateRequest = UserUpdateRequest.builder()
        .nickname("update-user-nickname")
        .password(null)
        .build();

    when(userRepository.existsByNickname(updateRequest.getNickname())).thenReturn(false);
    when(userRepository.save(user)).thenReturn(user);

    //when
    userService.updateUser(updateRequest, user);

    //then
    assertThat(user.getNickname(), is(updateRequest.getNickname()));
    assertNotNull(user.getPassword());

    verify(userRepository).existsByNickname(updateRequest.getNickname());
    verify(userRepository).save(user);
  }

  @Test
  void updateUserPasswordSuccessTest() {
    //given
    String newEncodedPassword = "$8a$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G";
    UserUpdateRequest updateRequest = UserUpdateRequest.builder()
        .nickname(null)
        .password("new-password")
        .build();

    when(passwordEncoder.encode(updateRequest.getPassword())).thenReturn(newEncodedPassword);
    when(userRepository.save(user)).thenReturn(user);

    //when
    userService.updateUser(updateRequest, user);

    //then
    assertNotNull(user.getNickname());
    assertThat(user.getPassword(), is(newEncodedPassword));

    verify(passwordEncoder).encode(updateRequest.getPassword());
    verify(userRepository).save(user);
  }

  @Test
  void updateUserNicknameAndPasswordSuccessTest() {
    //given
    String newEncodedPassword = "$8a$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G";
    UserUpdateRequest updateRequest = UserUpdateRequest.builder()
        .nickname("new-nickname")
        .password("new-password")
        .build();

    when(userRepository.existsByNickname(updateRequest.getNickname())).thenReturn(false);
    when(passwordEncoder.encode(updateRequest.getPassword())).thenReturn(newEncodedPassword);
    when(userRepository.save(user)).thenReturn(user);

    //when
    userService.updateUser(updateRequest, user);

    //then
    assertThat(user.getNickname(), is(updateRequest.getNickname()));
    assertThat(user.getPassword(), is(newEncodedPassword));

    verify(userRepository).existsByNickname(updateRequest.getNickname());
    verify(passwordEncoder).encode(updateRequest.getPassword());
    verify(userRepository).save(user);
  }

  @Test
  void updateUserUsingDuplicatedNicknameTest() {
    //given
    String newEncodedPassword = "$8a$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G";
    UserUpdateRequest updateRequest = UserUpdateRequest.builder()
        .nickname("nickname")
        .password("new-password")
        .build();

    when(passwordEncoder.encode(updateRequest.getPassword())).thenReturn(newEncodedPassword);
    when(userRepository.existsByNickname(updateRequest.getNickname())).thenReturn(true);

    //when
    assertThrows(DuplicateUserArgumentException.class,
        () -> userService.updateUser(updateRequest, user));

    //then
    assertThat(user.getNickname(), not(updateRequest.getNickname()));

    verify(passwordEncoder).encode(updateRequest.getPassword());
    verify(userRepository).existsByNickname(updateRequest.getNickname());
  }

  @Test
  public void checkPasswordSuccessTest() {
    //given
    String password = "password";

    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

    //when
    userService.checkPassword(user, password);

    //then
    verify(passwordEncoder).matches(password, user.getPassword());
  }

  @Test
  void checkPasswordFailTest() {
    //given
    User user1 = User.builder().nickname("user1")
        .location("seoul")
        .password("$8a$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G") //new-password
        .email("user1@test.com")
        .build();
    String password = "password";

    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

    //when
    assertThrows(InvalidPasswordException.class, () -> userService.checkPassword(user, password));

    //then
    verify(passwordEncoder).matches(password, user.getPassword());
  }

  @Test
  void getUserTest() {
    //given
    UserReadResponse response = UserReadResponse.builder()
        .email(user.getEmail())
        .nickname(user.getNickname())
        .build();
    when(userConverter.convertToUserReadResponse(user)).thenReturn(response);

    //when
    userService.getUser(user);

    //then
    verify(userConverter).convertToUserReadResponse(user);
  }

  @Test
  void getUserInfoTest() {
    //given
    UserInfoResponse response = UserInfoResponse.builder()
        .email(user.getEmail())
        .userId(user.getId())
        .nickname(user.getNickname())
        .build();

    when(userConverter.convertToUserInfoResponse(user)).thenReturn(response);

    //when
    userService.getUserInfo(user);

    //then
    verify(userConverter).convertToUserInfoResponse(user);
  }

}
