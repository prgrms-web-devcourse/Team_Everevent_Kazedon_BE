package kdt.prgrms.kazedon.everevent.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank
    String email;

    @Size(max = 20)
    String nickname;

    @Size(max = 100)
    String password;
}
