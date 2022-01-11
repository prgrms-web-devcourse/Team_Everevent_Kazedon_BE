package kdt.prgrms.kazedon.everevent.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.AlreadyFavoritedException;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

  @InjectMocks
  private CustomUserDetailService customUserDetailService;

  @Mock
  private UserRepository userRepository;

  private User user = User.builder()
      .email("test-email9@gmail.com")
      .password("test-password")
      .nickname("test-nickname")
      .location("test-location")
      .build();

  @Test
  public void loadUserByUsernameSuccessTest() {
    //given
    ReflectionTestUtils.setField(user, "id", 1L);
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

    //when
    customUserDetailService.loadUserByUsername(user.getEmail());

    //then
    verify(userRepository).findByEmail(user.getEmail());
  }

  @Test
  public void loadUserByUsernameNotExistTest() {
    //given
    ReflectionTestUtils.setField(user, "id", 1L);
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

    //when
    assertThrows(NotFoundException.class,
        () -> customUserDetailService.loadUserByUsername(user.getEmail()));

    //then
    verify(userRepository).findByEmail(user.getEmail());
  }

}
