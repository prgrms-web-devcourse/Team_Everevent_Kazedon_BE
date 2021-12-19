package kdt.prgrms.kazedon.everevent.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class UserReadResponse {

    private String email;
    private String nickname;

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("nickname")
    public String getNickname() {
        return nickname;
    }

}
