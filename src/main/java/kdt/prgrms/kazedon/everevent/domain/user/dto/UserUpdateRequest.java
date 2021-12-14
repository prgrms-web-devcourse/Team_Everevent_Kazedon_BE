package kdt.prgrms.kazedon.everevent.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank
    @Email
    private String email;

    @Size(max = 20)
    private String nickname;

    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9]{6,100}$")
    private String password;
}