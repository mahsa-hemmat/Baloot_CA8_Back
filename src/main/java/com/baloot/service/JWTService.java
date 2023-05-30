package com.baloot.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

public class JWTService {

    private final static String key="baloot123456";

    private final static String issuer="Baloot";
    private static SignatureAlgorithm algorithm;
    private static Key signKey;

    private static JWTService instance;

    public static byte[] getHash(String input){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }
        catch (NoSuchAlgorithmException exception) {
            System.out.println("Algorithm Does Not Exist: " + exception);
            return input.getBytes(StandardCharsets.UTF_8);
        }
    }
    private JWTService(){
        algorithm =SignatureAlgorithm.HS256;
        signKey = new SecretKeySpec(getHash(key), algorithm.getJcaName());
    }

    public static JWTService getInstance(){
        if(instance == null){
            instance = new JWTService();
        }
        return instance;
    }
    public String createJWT(String username){

        Instant now = Instant.now();

        JwtBuilder jwtBuilder = Jwts.builder()
                .claim("username", username)
                .setId(UUID.randomUUID().toString())
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(24L, ChronoUnit.HOURS)))
                .signWith(signKey, algorithm);

        return jwtBuilder.compact();
    }

    public Claims decodeJWT(String token){
        try {
            return Jwts.parser().setSigningKey(signKey).parseClaimsJws(token).getBody();
        }catch (Exception e){
            return null;
        }
    }

    public boolean validateJwt(Claims claims){
        if(!claims.getIssuer().equals(issuer)) return false;
        else if(claims.getExpiration().before(Date.from(Instant.now()))) return false;
        else return true;
    }
}
