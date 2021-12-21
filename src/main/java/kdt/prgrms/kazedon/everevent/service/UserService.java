package kdt.prgrms.kazedon.everevent.service;

import java.util.Arrays;
import java.util.List;
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
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserConverter userConverter;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public Long signUp(SignUpRequest request) {
    String encodedPassword = passwordEncoder.encode(request.getPassword());
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateUserArgumentException(ErrorMessage.DUPLICATE_EMAIL_ARGUMENT,
          request.getEmail());
    }
    if (userRepository.existsByNickname(request.getNickname())) {
      throw new DuplicateUserArgumentException(ErrorMessage.DUPLICATE_NICKNAME_ARGUMENT,
          request.getNickname());
    }
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

  public UserReadResponse getUser(User user) {
    return userConverter.convertToUserReadResponse(user);
  }

  @Transactional
  public Long updateUser(UserUpdateRequest updateRequest, User user) {
    if (updateRequest.getPassword() != null) {
      user.changePassword(
          passwordEncoder.encode(updateRequest.getPassword())
      );
    }

    if (updateRequest.getNickname() != null) {
      checkNicknameDuplicate(updateRequest.getNickname());
      user.changeNickname(updateRequest.getNickname());
    }

    return userRepository.save(user).getId();
  }

  public UserInfoResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(
            () -> new UnAuthorizedException(ErrorMessage.LOGIN_FAILED, request.getEmail()));
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new UnAuthorizedException(ErrorMessage.LOGIN_FAILED, request.getEmail());
    }

    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(), request.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authenticate);
    return UserInfoResponse.builder().userId(user.getId()).nickname(user.getNickname()).build();
  }

  public void checkPassword(User user, String password) {
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new InvalidPasswordException(ErrorMessage.INVALID_PASSWORD, user.getEmail());
    }
  }
}
