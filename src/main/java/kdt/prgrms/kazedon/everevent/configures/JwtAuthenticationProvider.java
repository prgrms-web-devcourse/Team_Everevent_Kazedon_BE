package kdt.prgrms.kazedon.everevent.configures;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import kdt.prgrms.kazedon.everevent.configures.property.JwtProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

  private final JwtProperty jwtProperty;

  private final UserDetailsService userDetailsService;

  public String createToken(String userPk, List<String> roles) {
    Claims claims = Jwts.claims().setSubject(userPk);
    claims.put("roles", roles);
    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + jwtProperty.getTokenValidTime()))
        .signWith(SignatureAlgorithm.HS256, jwtProperty.getSecretKey())
        .compact();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUserPk(String token) {
    return Jwts.parser().setSigningKey(jwtProperty.getSecretKey()).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest request) {
    String token = null;
    Cookie cookie = WebUtils.getCookie(request, "X-AUTH-TOKEN");
    if(cookie != null) token = cookie.getValue();
    return token;
  }

  public boolean validateToken(String jwtToken) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(jwtProperty.getSecretKey()).parseClaimsJws(jwtToken);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }
}
