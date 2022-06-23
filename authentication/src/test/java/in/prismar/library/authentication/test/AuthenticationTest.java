package in.prismar.library.authentication.test;

import in.prismar.library.authentication.AuthenticationProvider;
import in.prismar.library.authentication.jwt.JWTAuthenticationAdapter;
import io.fusionauth.jwt.JWTUtils;
import io.fusionauth.jwt.domain.KeyPair;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class AuthenticationTest {


    private AuthenticationProvider provider;

    @Before
    public void setup() {
        KeyPair pair = JWTUtils.generate2048_RSAKeyPair();
        provider = new JWTAuthenticationAdapter(pair.privateKey, pair.publicKey);
    }

    @Test
    public void testValidation() {
        final String token = provider.generateToken("Test", 30, "TestRole");
        Assertions.assertEquals(true, provider.authenticate(token).isSuccess());
    }

}
