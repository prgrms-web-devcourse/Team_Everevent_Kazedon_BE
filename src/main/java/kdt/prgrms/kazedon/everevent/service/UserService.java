package kdt.prgrms.kazedon.everevent.service;

import java.util.Arrays;
import java.util.List;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.UserType;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.UserReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.dto.UserUpdateRequest;
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
  public List<UserType> changeAuthorityToBusiness(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, email));
    user.addAuthority(UserType.ROLE_BUSINESS);
    List<UserType> roles = Arrays.stream(user.getRoles().split(","))
        .map(UserType::valueOf).toList();
    userRepository.save(user);
    return roles;
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
