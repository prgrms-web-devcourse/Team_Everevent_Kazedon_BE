package kdt.prgrms.kazedon.everevent.configures.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {

  private final String secretKey;

  private final int tokenValidTime;

}
