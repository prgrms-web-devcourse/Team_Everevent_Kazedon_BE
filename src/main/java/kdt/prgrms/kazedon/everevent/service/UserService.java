package kdt.prgrms.kazedon.everevent.service;

import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.UserType;
import kdt.prgrms.kazedon.everevent.domain.user.dto.request.LoginRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.request.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.request.UserUpdateRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.response.UserInfoResponse;
import kdt.prgrms.kazedon.everevent.domain.user.dto.response.UserReadResponse;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.exception.DuplicateUserArgumentException;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.InvalidPasswordException;
import kdt.prgrms.kazedon.everevent.exception.NotFoundException;
import kdt.prgrms.kazedon.everevent.exception.UnAuthorizedException;
import kdt.prgrms.kazedon.everevent.service.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserConverter userConverter;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public void signUp(SignUpRequest request) {
    checkEmailDuplicate(request.getEmail());
    checkNicknameDuplicate(request.getNickname());

    userRepository.save(
        userConverter.convertToUser(request, passwordEncoder.encode(request.getPassword())));
  }

  @Transactional(readOnly = true)
  public void checkEmailDuplicate(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DuplicateUserArgumentException(ErrorMessage.DUPLICATE_EMAIL_ARGUMENT, email);
    }
  }

  @Transactional(readOnly = true)
  public void checkNicknameDuplicate(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      throw new DuplicateUserArgumentException(ErrorMessage.DUPLICATE_NICKNAME_ARGUMENT, nickname);
    }
  }

  @Transactional
  public void changeAuthorityToBusiness(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUNDED, email));

    user.addAuthority(UserType.ROLE_BUSINESS);

    userRepository.save(user);
  }

  @Transactional
  public void updateUser(UserUpdateRequest updateRequest, User user) {
    if (updateRequest.getPassword() != null) {
      user.changePassword(
          passwordEncoder.encode(updateRequest.getPassword())
      );
    }

    if (updateRequest.getNickname() != null) {
      checkNicknameDuplicate(updateRequest.getNickname());
      user.changeNickname(updateRequest.getNickname());
    }

    userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public UserInfoResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(
            () -> new UnAuthorizedException(ErrorMessage.LOGIN_FAILED, request.getEmail()));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new UnAuthorizedException(ErrorMessage.LOGIN_FAILED, request.getEmail());
    }

    setAuthenticate(request.getEmail(), request.getPassword());

    return userConverter.convertToUserInfoResponse(user);
  }

  private void setAuthenticate(String email, String password) {
    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password));
    SecurityContextHolder.getContext().setAuthentication(authenticate);
  }

  public void checkPassword(User user, String password) {
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new InvalidPasswordException(ErrorMessage.INVALID_PASSWORD, user.getEmail());
    }
  }

  public UserInfoResponse getUserInfo(User user) {
    return userConverter.convertToUserInfoResponse(user);
  }

  public UserReadResponse getUser(User user) {
    return userConverter.convertToUserReadResponse(user);
  }

}
