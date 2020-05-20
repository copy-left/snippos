package com.copyleft.snippos.security;

import com.copyleft.snippos.config.AuthProperties;
import com.copyleft.snippos.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private static final String USER_ID = "uid";
    private static final String AUTHORITIES_KEY = "auth";

    @Autowired
    private AuthProperties authProperties;

    @Autowired
    private UserService userService;

    /**
     * Create Token once authentication is successful
     *
     * @param authentication
     * @param rememberMe
     * @return
     */
    public String createToken(Authentication authentication, Boolean rememberMe) {
        Long companyId = null;
        String authorities = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + authProperties.getAuthentication().getJwt().getTokenValidityInMilliSecondsForRememberMe());
        } else {
            validity = new Date(now + authProperties.getAuthentication().getJwt().getTokenValidityInMilliSeconds());
        }
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(USER_ID, userService.findCurrentLoggedInUser().get().getId())
                .signWith(SignatureAlgorithm.HS512, authProperties.getAuthentication().getJwt().getSecret())
                .setExpiration(validity)
                .compact();
    }

    /**
     * Build Authentication object from json web token
     *
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(authProperties.getAuthentication().getJwt().getSecret())
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.asList(claims.get(AUTHORITIES_KEY).toString().split(",")).stream()
                        .map(authority -> new SimpleGrantedAuthority(authority))
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "",
                authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * Validate a jwt Token
     *
     * @param authToken
     * @return
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(authProperties.getAuthentication().getJwt().getSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature: " + e.getMessage());
            return false;
        }
    }

}
