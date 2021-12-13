package kdt.prgrms.kazedon.everevent.service.converter;

import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.dto.UserReadResponse;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public UserReadResponse convertToUserReadResponse(User user){
        return UserReadResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}