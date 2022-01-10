package kdt.prgrms.kazedon.everevent.service.converter;

import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.dto.request.SignUpRequest;
import kdt.prgrms.kazedon.everevent.domain.user.dto.response.UserInfoResponse;
import kdt.prgrms.kazedon.everevent.domain.user.dto.response.UserReadResponse;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

  public UserReadResponse convertToUserReadResponse(User user) {
    return UserReadResponse.builder()
        .email(user.getEmail())
        .nickname(user.getNickname())
        .build();
  }

  public UserInfoResponse convertToUserInfoResponse(User user) {
    return UserInfoResponse.builder()
        .userId(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .build();
  }

  public User convertToUser(SignUpRequest request, String encodedPassword) {
    return User.builder()
        .email(request.getEmail())
        .password(encodedPassword)
        .nickname(request.getNickname())
        .location("")
        .build();
  }

}
