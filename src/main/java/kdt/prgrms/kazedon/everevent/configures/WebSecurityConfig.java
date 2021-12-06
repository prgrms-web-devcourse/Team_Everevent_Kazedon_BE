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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;

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
        .authorizeRequests()
          .antMatchers("/api/*/login").permitAll()
          .antMatchers("/api/*/signup/**").permitAll()
          .anyRequest().permitAll().and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationProvider),
            UsernamePasswordAuthenticationFilter.class);

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
