package com.flatlogic.app.generator.jwt;

import com.flatlogic.app.generator.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

/**
 * JwtTokenUtil instance.
 */
@Component
public class JwtTokenUtil {

    private static final String SECRET = UUID.randomUUID().toString();
    private static final String USER = "user";

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.token.expiration.hours:10}")
    private Long hours;

    /**
     * Extract username.
     *
     * @param token Token
     * @return Username string
     */
    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration from token.
     *
     * @param token Token string
     * @return Date
     */
    public Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract claim from token.
     *
     * @param token          Token string
     * @param claimsResolver Claims resolver
     * @param <T>            Type class
     * @return Claim
     */
    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generate token.
     *
     * @param username User name
     * @return Token string
     */
    public String generateToken(final String username) {
        var user = userRepository.findByEmail(username);
        long current = System.currentTimeMillis();
        return Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE).setSubject(username)
                .claim(USER, new JwtUser(user.getId(), user.getEmail()))
                .setIssuedAt(new Date(current)).setExpiration(new Date(current + 1000L * 60L * 60L * hours))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }

    /**
     * Validate token.
     *
     * @param token       Token string
     * @param userDetails UserDetails
     * @return result
     */
    public boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = extractUsername(token);
        return (Objects.equals(username, userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims extractAllClaims(final String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

}

