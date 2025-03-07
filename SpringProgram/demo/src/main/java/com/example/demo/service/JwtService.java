package com.example.demo.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;


@Component
/**
 * Service responsible for handling JWT operations such as generating, validating, 
 * and extracting claims from JWT tokens. It supports user authentication and password reset functionality.
 * This service uses a secret key to sign and validate JWTs and provides methods to extract user-related 
 * information and additional claims (like reset tokens).
 */
public class JwtService {

    Random rand = new Random(); // Used to generate random number for reset token

    public static final String SECRET = "123456789eaaaaaaaaaaaaaaaaaaaaaasdawdawdawdawdawdadawdawdawdadawdawd"; //to be replaced
    
    /**
     * Generates a JWT token for the specified user. 
     * @param userName The username for the user to be included in the token's claims as the subject.
     * @return The generated JWT token.
     */
    public String generateToken(String userName){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,userName);
    }
    
    /**
     * Creates a JWT token with specified claims and username.
     * @param claims A map of claims to include in the token.
     * @param userName The username for the user.
     * @return The generated JWT token.
     */
    private String createToken(Map<String,Object> claims, String userName){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*30))    // valid token lasts 30 min
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Retrieves the signing key used to sign the JWT token.
     * @return The signing key.
     */
    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts all claims from the JWT token.
     * @param token The JWT token.
     * @return The claims extracted from the token.
     */
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts a specific claim from the JWT token.
     * @param <T> The type of the claim to be extracted.
     * @param token The JWT token.
     * @param claimsResolver A function to extract the claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts the subject from the JWT token. This can be, for example, a username or email
     * @param token The JWT token.
     * @return The subject extracted from the token.
     */
    public String extractSubject(String token){
        return extractClaim(token,Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     * @param token The JWT token.
     * @return The expiration date of the token.
     */
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Checks if the JWT token is expired.
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates the JWT token by comparing the username and expiration date.
     * @param token The JWT token.
     * @param userDetails The user details to compare against the token.
     * @return True if the token is valid, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = extractSubject(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    /**
     * Generates a unique token for the purpose of creating a unique reset link
     * @param email The email for the person this reset token is for, which is used as the subject of the token
     * @return The genereated JWT token
     */
    public String generateResetToken(String email){
        Map<String,Object> claims = new HashMap<>();
        claims.put("randomNumber", rand.nextLong());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*5))    // valid token lasts 5 min
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * This extract the randomNumber claim from a jwt, or null if no such claim exists
     * @param token The token to extract the claim from
     * @return The random number, or null if no such number exists
     */
    public Long extractRandomNumber(String token){
        return (Long)extractAllClaims(token).get("randomNumber");
    }

    /**
     * This method is used to validate that a specific token could have been generated by this application
     * @param token The token to validate
     * @return A boolean representing if this is a possibly valid token
     */
    public Boolean validateResetToken(String token){
        return (!isTokenExpired(token));
    }
}
