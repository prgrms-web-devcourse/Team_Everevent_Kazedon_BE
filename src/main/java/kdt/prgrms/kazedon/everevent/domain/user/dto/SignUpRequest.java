package kdt.prgrms.kazedon.everevent.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

  @NotBlank(message = "이메일을 작성해주세요.")
  @Email(message = "이메일 형식이 올바르지 않습니다.")
  private String email;

  @NotBlank(message = "비밀번호를 작성해주세요.")
  private String password;

  @NotBlank(message = "닉네임을 작성해주세요.")
  private String nickname;

  public void encodingPassword(String encode) {
    password = encode;
  }
}
