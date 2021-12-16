package kdt.prgrms.kazedon.everevent.configures;

import kdt.prgrms.kazedon.everevent.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

  private final CustomUserDetailService customUserDetailService;

  public JwtAuthenticationProvider jwtAuthenticationProvider() {
    return new JwtAuthenticationProvider(customUserDetailService);
  }

  private final CorsFilter corsFilter;

  public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(),jwtAuthenticationProvider());
    jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
    return jwtAuthenticationFilter;
  }


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
        .addFilter(jwtAuthenticationFilter())
        .addFilter(new JwtAuthorizationFilter(authenticationManager(),jwtAuthenticationProvider()))
        .authorizeRequests()
          .antMatchers("/api/v1/login").permitAll()
          .antMatchers("/api/v1/signup/**").permitAll()
          .antMatchers("/api/v1/favorites/**")
            .access("hasRole('ROLE_USER') or hasRole('ROLE_BUSINESS')")
          .anyRequest().permitAll();
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
