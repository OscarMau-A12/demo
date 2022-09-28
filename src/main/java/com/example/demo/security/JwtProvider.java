package com.example.demo.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication authentication){
        UserDetails localUser = (UserDetails) authentication.getPrincipal();
        return Jwts.builder().setSubject((localUser.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //Function that allows getting claims from token
    public String getUserNameFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    //Function created to control invalidations
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException e){
            log.error("MalFormed jwt");
        }catch (UnsupportedJwtException e){
            log.error("Unsupported jwt");
        }catch (ExpiredJwtException e){
            log.error("Expired jwt");
        }catch (IllegalArgumentException e){
            log.error("Illegal jwt");
        }catch (SignatureException e){
            log.error("Fail on signature jwt");
        }
        return false;
    }

}
