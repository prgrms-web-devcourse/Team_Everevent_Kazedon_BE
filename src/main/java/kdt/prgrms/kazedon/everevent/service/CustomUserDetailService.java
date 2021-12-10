package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.configures.auth.CustomUserDetails;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SimpleUserResponse;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository
        .findByEmail(email)
        .map(CustomUserDetails::new)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, email));
  }

  @Transactional
  public Long signUp(SignUpRequest request){
    return userRepository.save(new User(request)).getId();
  }

  public SimpleUserResponse findByEmail(String email){
    return userRepository.findByEmail(email).map(SimpleUserResponse::new)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, email));
  }
}
