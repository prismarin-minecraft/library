package in.prismar.library.auth.jwt;

import in.prismar.library.auth.AuthProvider;
import in.prismar.library.auth.AuthResult;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.rsa.RSASigner;
import io.fusionauth.jwt.rsa.RSAVerifier;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class JWTAuthAdapter implements AuthProvider {

    private final String privateKey;
    private final String publicKey;

    private Signer signer;
    private Verifier verifier;

    public JWTAuthAdapter(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;

        if(!privateKey.isBlank()) {
            this.signer = RSASigner.newSHA256Signer(privateKey);
        }
        if(!publicKey.isBlank()) {
            this.verifier = RSAVerifier.newVerifier(publicKey);
        }
    }

    @Override
    public String generateToken(String subject, long expiration, String... roles) {
        JWT jwt = new JWT()
                .setSubject(subject)
                .setIssuedAt(ZonedDateTime.now())
                .setExpiration(ZonedDateTime.now().plusSeconds(expiration))
                .addClaim("roles", Arrays.asList(roles));
        String encoded = JWT.getEncoder().encode(jwt, signer);
        return encoded;
    }

    @Override
    public AuthResult auth(String token) {
        try {
            JWT jwt = JWT.getDecoder().decode(token, verifier);
            List<String> roles = (List<String>) jwt.getObject("roles");
            return new AuthResult(true, jwt.subject, jwt.expiration.toInstant().toEpochMilli(), roles);
        }catch (Exception exception) {}
        return new AuthResult(false, null, -1, null);
    }
}
