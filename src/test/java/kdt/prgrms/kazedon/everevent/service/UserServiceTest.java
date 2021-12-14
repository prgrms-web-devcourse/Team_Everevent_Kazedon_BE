package kdt.prgrms.kazedon.everevent.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.UserUpdateRequest;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.DuplicateUserArgumentException;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.UserConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
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

  private SignUpRequest signUpRequest;

  private User user;

  private String userEmail;

  private String encodedPassword = "$2b$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G";

  @BeforeEach
  public void setUp(){
    userEmail = "test-user@gmail.com";
    signUpRequest = SignUpRequest.builder()
        .email(userEmail)
        .nickname("user-nickname")
        .password("paaword") //password
        .build();
    user = new User(signUpRequest, encodedPassword);
    userRepository.save(user);
    ReflectionTestUtils.setField(user, "id", 1L);
  }

  @Test
  void signUp() {
    //Given
    String signupEmail = "test-user2@gmail.com";
    String encodedPassword2 = "$2b$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G";
    SignUpRequest signUpRequest2 = SignUpRequest.builder()
        .email(signupEmail)
        .nickname("user-nickname2")
        .password("new-password")
        .build();
    User user2 = new User(signUpRequest2, encodedPassword2);

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
  void updateUserNickname(){
    //Given
    Long userId = 1L;
    UserUpdateRequest updateRequest = UserUpdateRequest.builder()
            .nickname("update-user-nickname")
            .password(null)
            .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.existsByNickname(updateRequest.getNickname())).thenReturn(false);
    when(userRepository.save(user)).thenReturn(user);

    //When
    userService.updateUser(updateRequest, userId);
    log.debug("user => nickname : {}, password : {}", user.getNickname(), user.getPassword());

    //Then
    assertThat(user.getNickname(), is(updateRequest.getNickname()));
    assertNotNull(user.getPassword());

    verify(userRepository).findById(userId);
    verify(userRepository).existsByNickname(updateRequest.getNickname());
    verify(userRepository, times(2)).save(user);
  }

  @Test
  void updateUserPassword(){
    //Given
    Long userId = 1L;
    String newEncodedPassword = "$8a$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G";
    UserUpdateRequest updateRequest = UserUpdateRequest.builder()
            .nickname(null)
            .password("new-password")
            .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(passwordEncoder.encode(updateRequest.getPassword())).thenReturn(newEncodedPassword);
    when(userRepository.save(user)).thenReturn(user);

    //When
    userService.updateUser(updateRequest, userId);
    log.debug("user => nickname : {}, password : {}", user.getNickname(), user.getPassword());

    //Then
    assertNotNull(user.getNickname());
    assertThat(user.getPassword(), is(newEncodedPassword));

    verify(userRepository).findById(userId);
    verify(passwordEncoder).encode(updateRequest.getPassword());
    verify(userRepository, times(2)).save(user);
  }

  @Test
  void updateUserNicknameAndPassword(){
    //Given
    Long userId = 1L;
    String newEncodedPassword = "$8a$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G";
    UserUpdateRequest updateRequest = UserUpdateRequest.builder()
            .nickname("new-nickname")
            .password("new-password")
            .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.existsByNickname(updateRequest.getNickname())).thenReturn(false);
    when(passwordEncoder.encode(updateRequest.getPassword())).thenReturn(newEncodedPassword);
    when(userRepository.save(user)).thenReturn(user);

    //When
    userService.updateUser(updateRequest, userId);
    log.debug("user => nickname : {}, password : {}", user.getNickname(), user.getPassword());

    //Then
    assertThat(user.getNickname(), is(updateRequest.getNickname()));
    assertThat(user.getPassword(), is(newEncodedPassword));

    verify(userRepository).findById(userId);
    verify(userRepository).existsByNickname(updateRequest.getNickname());
    verify(passwordEncoder).encode(updateRequest.getPassword());
    verify(userRepository, times(2)).save(user);
  }

  @Test
  void updateUserUsingDuplicatedNickname(){
    //Given
    Long userId = 1L;
    String newEncodedPassword = "$8a$10$ux4JoQBz5AIFWCGh.TdgDuGyOjXpW2oJ3EO7qjbLZ5HTfdynvM34G";
    UserUpdateRequest updateRequest = UserUpdateRequest.builder()
            .nickname("new-nickname")
            .password("new-password")
            .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(passwordEncoder.encode(updateRequest.getPassword())).thenReturn(newEncodedPassword);
    when(userRepository.existsByNickname(updateRequest.getNickname())).thenReturn(true);

    //When
    assertThrows(DuplicateUserArgumentException.class, () -> userService.updateUser(updateRequest, userId));
    log.debug("user => nickname : {}, password : {}", user.getNickname(), user.getPassword());

    //Then
    assertThat(user.getNickname(), not(updateRequest.getNickname()));

    verify(userRepository).findById(userId);
    verify(passwordEncoder).encode(updateRequest.getPassword());
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void updateNonExistingUser(){
    //Given
    Long invalidUserId = Long.MAX_VALUE;
    UserUpdateRequest updateRequest = UserUpdateRequest.builder()
            .nickname("new-nickname")
            .password("new-password")
            .build();

    when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

    //When
    //Then
    assertThrows(NotFoundException.class, () -> userService.updateUser(updateRequest, invalidUserId));
    verify(userRepository).findById(invalidUserId);
  }
}
