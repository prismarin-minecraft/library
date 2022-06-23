package in.prismar.library.authentication;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface AuthenticationProvider {

    String generateToken(String subject, long expiration, String... roles);

    AuthenticationResult authenticate(String token);
}
