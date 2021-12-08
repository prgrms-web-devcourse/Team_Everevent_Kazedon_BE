package kdt.prgrms.kazedon.everevent.configures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;


  public JwtAuthenticationFilter jwtAuthorizationFilter() throws Exception {
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(),jwtAuthenticationProvider);
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
        .cors().and()
        .csrf().disable()
        .httpBasic().disable()
        .formLogin().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtAuthenticationProvider))
        .addFilter(jwtAuthorizationFilter())
        .authorizeRequests()
          .antMatchers("/api/v1/login").permitAll()
          .antMatchers("/api/v1/signup/**").permitAll()
          .antMatchers("/api/v1/user/**")
            .access("hasRole('ROLE_USER') or hasRole('ROLE_BUSINESS') or hasRole('ROLE_ADMIN')")
          .antMatchers("/api/v1/business/**")
            .access("hasRole('ROLE_BUSINESS') or hasRole('ROLE_ADMIN')")
          .antMatchers("/api/v1/admin/**")
            .access("hasRole('ROLE_ADMIN')")
          .anyRequest().permitAll();

  }

  @Override
  public void configure(WebSecurity web) throws Exception {
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
