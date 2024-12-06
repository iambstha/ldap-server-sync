package org.base.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.base.dto.UserInfoResDto;
import org.base.exception.BadRequestException;
import org.base.model.User;
import org.base.repository.UserRepository;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.function.Function;

@Singleton
public class JwtUtil {

    public static final long TOKEN_EXPIRATION_TIME = 50 * 60 * 1000; // 50 minutes in milliseconds

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @Inject
    private UserRepository userRepository;

    @PostConstruct
    public void init() throws Exception {
        String privateKeyContent = new String(Files.readAllBytes(Paths.get("./src/main/resources/privateKey.pem")))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = keyFactory.generatePrivate(privateKeySpec);

        String publicKeyContent = new String(Files.readAllBytes(Paths.get("./src/main/resources/publicKey.pem")))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyContent);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        this.publicKey = keyFactory.generatePublic(publicKeySpec);
    }

    public String generateToken(Long userId) {

        User user = userRepository.findById(userId);

        Set<String> permissions = new HashSet<>(List.of(new String[]{"SUPER ADMIN"}));

        return Jwt.issuer("auth-service")
                .upn("custom-auth")
                .groups(permissions)
                .claim("userId", userId)
                .claim("firstName",user.getFirstName())
                .claim("lastName",user.getLastName())
                .subject(userId.toString())
                .expiresIn(TOKEN_EXPIRATION_TIME)
                .sign();

    }

    public UserInfoResDto getUserTokenResDto(String token){
        if(verifyToken(token)){
            Object userIdClaim = extractAllClaims(token).get("userId");
            if (userIdClaim == null) {
                return null;
            }
            String firstName = String.valueOf(extractAllClaims(token).get("firstName"));
            String lastName = String.valueOf(extractAllClaims(token).get("lastName"));

            return UserInfoResDto.builder()
                    .userId((Long.valueOf(userIdClaim.toString())))
                    .userName(extractUsername(token))
                    .firstName(firstName)
                    .lastName(lastName)
                    .build();
        }
        throw new BadRequestException("Invalid token");

    }

    public boolean verifyToken(String token){
        return isTokenValid(token);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction
                .apply(Jwts
                        .parser()
                        .verifyWith(publicKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                );
    }



    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    public boolean isTokenValid(String token) {
        return (!isTokenExpired(token));
    }

}
