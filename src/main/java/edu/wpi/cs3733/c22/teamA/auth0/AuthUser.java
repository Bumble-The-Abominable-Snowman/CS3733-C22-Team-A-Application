package edu.wpi.cs3733.c22.teamA.auth0;

import com.auth0.jwk.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;

public class AuthUser {

    private String token = "";
    private ArrayList<String> permissions = new ArrayList<>();

    public AuthUser() {

    }

    public AuthUser(String token) {
        this.token = token;
        System.out.printf("Init token is: %s\n", this.token);

        JwkProvider provider = new UrlJwkProvider(String.format("https://%s/", Auth0Login.DOMAIN));
        try {
            DecodedJWT jwt = JWT.decode(this.token);
            // Get the kid from received JWT token
            Jwk jwk = provider.get(jwt.getKeyId());


            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(String.format("https://%s/", Auth0Login.DOMAIN))
                    .build();

            jwt = verifier.verify(this.token);
            System.out.println(jwt.getClaims());
            if (!jwt.getClaim("permissions").isNull())
            {
                this.permissions.addAll(jwt.getClaim("permissions").asList(String.class));
                System.out.println(this.permissions);
            }
        } catch (JWTVerificationException | JwkException e){
            //Invalid signature/claims
            e.printStackTrace();
        }


//        JWTUtils.verifyToken(token).map(jwt -> {
//            for (String key: jwt.getClaims().keySet()) {
//                System.out.printf("openid Key: %s\tValue: %s\n", key,jwt.getClaim(key).asString());
//            }
//            return 1;
//        });
    }


    public String getToken()
    {
        return this.token;
    }

}
