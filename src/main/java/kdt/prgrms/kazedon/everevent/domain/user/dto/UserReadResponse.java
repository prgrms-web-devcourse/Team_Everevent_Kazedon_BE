package kdt.prgrms.kazedon.everevent.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserReadResponse {
    private String email;
    private String nickname;
}
