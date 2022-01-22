package kdt.prgrms.kazedon.everevent.configures;

import kdt.prgrms.kazedon.everevent.configures.property.JwtProperty;
import kdt.prgrms.kazedon.everevent.service.CustomUserDetailService;
import kdt.prgrms.kazedon.everevent.service.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtProperty jwtProperty;
  private final CustomUserDetailService customUserDetailService;
  private final CorsFilter corsFilter;
  private final UserConverter userConverter;

  public JwtAuthenticationProvider jwtAuthenticationProvider() {
    return new JwtAuthenticationProvider(jwtProperty, customUserDetailService);
  }

  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  private JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
        authenticationManagerBean(), jwtAuthenticationProvider(), userConverter);
    jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
    return jwtAuthenticationFilter;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
    authenticationManagerBuilder
        .userDetailsService(customUserDetailService)
        .passwordEncoder(passwordEncoder());
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(
        "/api-docs",
        "/webjars/**",
        "/h2-console/**");
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
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
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

}
