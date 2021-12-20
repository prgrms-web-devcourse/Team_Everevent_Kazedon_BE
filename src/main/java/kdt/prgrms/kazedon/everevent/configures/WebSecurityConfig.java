package kdt.prgrms.kazedon.everevent.configures;

import kdt.prgrms.kazedon.everevent.configures.property.JwtProperty;
import kdt.prgrms.kazedon.everevent.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtProperty jwtProperty;

  private final CustomUserDetailService customUserDetailService;

  public JwtAuthenticationProvider jwtAuthenticationProvider() {
    return new JwtAuthenticationProvider(jwtProperty, customUserDetailService);
  }

  private final CorsFilter corsFilter;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
          .addFilter(corsFilter)
        .httpBasic().disable()
        .formLogin().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtAuthenticationProvider()))
        .authorizeRequests()
          .antMatchers("/api/v1/signup")
            .anonymous()
          .antMatchers("/api/v1/logout")
            .access("hasRole('ROLE_USER') or hasRole('ROLE_BUSINESS')")
          .antMatchers("/api/v1/login")
            .anonymous()
          .antMatchers("/api/v1/likes/**")
            .access("hasRole('ROLE_USER')")
          .antMatchers("/api/v1/favorites/**")
            .access("hasRole('ROLE_USER')")
          .antMatchers("/api/v1/members/**")
            .access("hasRole('ROLE_USER') or hasRole('ROLE_BUSINESS')")
          .antMatchers("/api/v1/markets/*/events")
            .access("hasRole('ROLE_BUSINESS')")
          .antMatchers(HttpMethod.GET, "/api/v1/markets/*")
            .access("hasRole('ROLE_USER') or hasRole('ROLE_BUSINESS')")
          .antMatchers(HttpMethod.PATCH, "/api/v1/markets/*")
            .access("hasRole('ROLE_BUSINESS')")
          .antMatchers(HttpMethod.POST, "/api/v1/markets")
            .access("hasRole('ROLE_USER')")
          .antMatchers(HttpMethod.GET, "/api/v1/markets")
            .access("hasRole('ROLE_BUSINESS')")
          .antMatchers("/api/v1/events/*/participants")
            .access("hasRole('ROLE_USER')")
          .antMatchers(HttpMethod.POST, "/api/v1/events/*/reviews")
            .access("hasRole('ROLE_USER')")
          .antMatchers(HttpMethod.PATCH, "/api/v1/events/*")
            .access("hasRole('ROLE_BUSINESS')")
          .antMatchers(HttpMethod.POST, "/api/v1/events")
            .access("hasRole('ROLE_BUSINESS')")
          .anyRequest()
            .permitAll();
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(
        "/api-docs",
        "/webjars/**",
        "/h2-console/**");
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}
