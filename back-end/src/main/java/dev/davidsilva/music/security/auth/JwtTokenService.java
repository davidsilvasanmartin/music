package dev.davidsilva.music.security.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JwtTokenService {
    private final Duration jwtTokenDuration = Duration.ofMinutes(20);
    private final Algorithm jwtAlgorithm;
    private final JWTVerifier jwtVerifier;

    public JwtTokenService(@Value("${jwt.secret}") String secret) {
        this.jwtAlgorithm = Algorithm.HMAC512(secret);
        this.jwtVerifier = JWT.require(jwtAlgorithm).build();
    }

    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(now.plus(jwtTokenDuration))
                .withArrayClaim(
                        "permissions",
                        userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new)
                )
                .sign(jwtAlgorithm);
    }

    public String validateTokenAndGetUsername(String token) {
        try {
            return jwtVerifier.verify(token).getSubject();
        } catch (JWTVerificationException e) {
            // TODO log
            return null;
        }
    }
}
