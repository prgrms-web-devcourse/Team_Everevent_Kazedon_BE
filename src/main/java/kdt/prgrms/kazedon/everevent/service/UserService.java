package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.user.Authority;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.UserType;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.UserReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.dto.UserUpdateRequest;
import kdt.prgrms.kazedon.everevent.domain.user.repository.AuthorityRepository;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.DuplicateUserArgumentException;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.service.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  private final AuthorityRepository authorityRepository;

  private final PasswordEncoder passwordEncoder;

  private final UserConverter userConverter;

  @Transactional
  public Long signUp(SignUpRequest request) {
    String encodedPassword = passwordEncoder.encode(request.getPassword());
    return userRepository.save(new User(request, encodedPassword)).getId();
  }

  public void checkEmailDuplicate(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DuplicateUserArgumentException(ErrorMessage.DUPLICATE_EMAIL_ARGUMENT, email);
    }
  }

  public void checkNicknameDuplicate(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      throw new DuplicateUserArgumentException(ErrorMessage.DUPLICATE_NICKNAME_ARGUMENT, nickname);
    }
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

  public UserReadResponse getUser(Long userId){
    User retrievedUser = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, userId));

    return userConverter.convertToUserReadResponse(retrievedUser);
  }

  @Transactional
  public Long updateUser(UserUpdateRequest updateRequest, Long userId){
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, userId));

    if(updateRequest.getPassword() != null){
      user.changePassword(
              passwordEncoder.encode(updateRequest.getPassword())
      );
    }

    if(updateRequest.getNickname() != null){
      checkNicknameDuplicate(updateRequest.getNickname());
      user.changeNickname(updateRequest.getNickname());
    }

    return userRepository.save(user).getId();
  }
}
