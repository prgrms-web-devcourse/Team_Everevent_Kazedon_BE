package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.configures.auth.CustomUserDetails;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.dto.LoginRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailService(UserRepository repository) {
    this.userRepository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository
        .findByEmail(email)
        .map(CustomUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("not found User"));
  }

  @Transactional
  public Long signUp(SignUpRequest request){
    return userRepository.save(new User(request)).getId();
  }

  public User findByEmail(String email){
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("이메일을 가진 User를 찾을 수 없습니다."));
  }

  public boolean checkEmailAndPassword(LoginRequest request){
    User user = findByEmail(request.getEmail());

    if (request.getPassword().equals(user.getPassword())){
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    return true;
  }


}
