package kdt.prgrms.kazedon.everevent.configures;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

  @Bean
  public CorsFilter corsFilter(){
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("http://localhost:3000");
    config.addAllowedOrigin("https://everevent-be69d.web.app");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    config.addExposedHeader("X-AUTH-TOKEN");
    source.registerCorsConfiguration("/api/v1/**", config);

    return new CorsFilter(source);
  }
  
}
