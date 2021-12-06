package kdt.prgrms.kazedon.everevent.configures;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .exposedHeaders("X-AUTH-TOKEN")
        .allowCredentials(true)
        .allowedOrigins("http://localhost:3000");
  }
}
