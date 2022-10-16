package in.prismar.library.auth;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface AuthProvider {

    String generateToken(String subject, long expiration, String... roles);

    AuthResult auth(String token);
}
