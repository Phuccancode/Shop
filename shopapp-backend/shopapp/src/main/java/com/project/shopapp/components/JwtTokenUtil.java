package com.project.shopapp.components;

import com.project.shopapp.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.expiration}")
    private int expiration; //save to an env variable
    @Value("${jwt.secret}")
    private String secretKey;
    public String generateToken(User user) throws IllegalArgumentException{
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber",user.getPhoneNumber());
        try{
            String token = Jwts.builder()
                    .claims(claims)
                    .subject(user.getPhoneNumber())
                    .expiration(new Date(System.currentTimeMillis() + expiration*1000L))
                    .signWith(getSigningKey())
                    .compact();
            return token;
        }catch (Exception e){
            throw new IllegalArgumentException("Cannot generate token, error: "+e.getMessage());
            //return null;
        }
    }
    private SecretKey getSigningKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private Claims extractAllClaims(String token) throws IllegalArgumentException{
        try{
            return Jwts.parser()
                    .verifyWith(getSigningKey()) //verify signature
                    .build()
                    .parseSignedClaims(token) //parse token to extract claims
                    .getPayload();
        }catch (Exception e){
            throw new IllegalArgumentException("Cannot extract claims, error: "+e.getMessage());
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    //check expiration
    private Boolean isTokenExpired(String token){
        Date expiredDate = extractClaim(token, Claims::getExpiration);
        return expiredDate.before(new Date());
    }
    public String extractPhoneNumber(String token){
        return extractClaim(token, Claims::getSubject);
    }
}
