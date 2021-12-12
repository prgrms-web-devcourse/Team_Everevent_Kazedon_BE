package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.user.Authority;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.UserType;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SimpleUserResponse;
import kdt.prgrms.kazedon.everevent.domain.user.repository.AuthorityRepository;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  private final AuthorityRepository authorityRepository;

  private final PasswordEncoder passwordEncoder;

  @Transactional
  public Long signUp(SignUpRequest request) {
    return userRepository.save(new User(encodingPassword(request))).getId();
  }

  public SimpleUserResponse findByEmail(String email) {
    return userRepository.findByEmail(email).map(SimpleUserResponse::new)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, email));
  }

  public boolean checkEmailDuplicate(String email) {
    return userRepository.existsByEmail(email);
  }

  public boolean checkNicknameDuplicate(String nickname) {
    return userRepository.existsByNickname(nickname);
  }

  @Transactional
  public UserType changeAuthorityToBusiness(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, email));
    authorityRepository.deleteByUserId(user.getId());
    user.changeAuthority(UserType.ROLE_BUSINESS);
    Authority authority = userRepository.save(user).getAuthority().get(0);
    return UserType.valueOf(authority.getAuthorityName());
  }

  public SignUpRequest encodingPassword(SignUpRequest request) {
    request.encodingPassword(passwordEncoder.encode(request.getPassword()));
    return request;
  }
}
