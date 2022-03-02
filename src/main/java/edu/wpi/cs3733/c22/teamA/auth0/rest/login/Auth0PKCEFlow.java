package edu.wpi.cs3733.c22.teamA.auth0.rest.login;

import com.auth0.AuthorizeUrl;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.Request;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.auth0.UserInfo;
import edu.wpi.cs3733.c22.teamA.auth0.rest.ClientAuthenticationCredentials;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.Unchecked;

/**
 * https://auth0.com/docs/flows/guides/auth-code-pkce/add-login-auth-code-pkce#example-authorization-url
 *
 * @author Eduardo
 */
public class Auth0PKCEFlow {

    public static class FlowInfo {

        private final String verifier;
        private final String challenge;
        private final String state;
        private final String authorizeUrl;

        public FlowInfo(String verifier, String challenge, String state, String authorizeUrl) {
            this.verifier = verifier;
            this.challenge = challenge;
            this.state = state;
            this.authorizeUrl = authorizeUrl;
        }

        public String getVerifier() {
            return verifier;
        }

        public String getChallenge() {
            return challenge;
        }

        public String getState() {
            return state;
        }

        public String getAuthorizeUrl() {
            return authorizeUrl;
        }

    }

    public static final String DOMAIN = "dev-x7bjt62i.us.auth0.com";
    public static final String CLIENT_ID = "1vU3krUnEN7icaE4EHT8lEtQLFfzyR0Y";
    public static final String CLIENT_SECRET = "uOp5D53UbuLu-qJEPUWMPrx4HzNrsd5lSBHMjkq_OP1vn6hhLa_UR6LGyLcHpAz_";
    public static final String REDIRECT_URI = "https://vigorous-payne-ebb69d.netlify.app/";

    public static String createCodeVerifier() {
        final SecureRandom sr = new SecureRandom();
        final byte[] code = new byte[32];
        sr.nextBytes(code);

        return Base64.encodeBase64URLSafeString(code);
    }

    public static String createCodeChallenge(final String codeVerifier) {
        final byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        final MessageDigest md = Unchecked.supplier(() -> MessageDigest.getInstance("SHA-256")).get();
        md.update(bytes, 0, bytes.length);

        return Base64.encodeBase64URLSafeString(md.digest());
    }

    public static String createAuthorizationURL(final String challenge, final String state) {

        return "https://" + DOMAIN + "/authorize"
                + "?client_id=" + CLIENT_ID
                + "&response_type=code"
                + "&code_challenge_method=S256"
                + "&code_challenge=" + challenge
                + "&scope=openid profile email"
                + "&state=" + state
                + "&redirect_uri=" + REDIRECT_URI;
    }

    public static FlowInfo createAuthorizationFlow() {
        final String verifier = createCodeVerifier();
        final String challenge = createCodeChallenge(verifier);
        final String state = RandomStringUtils.randomAlphanumeric(8);
        final String authorizationURL = createAuthorizationURL(challenge, state);

        return new FlowInfo(verifier, challenge, state, authorizationURL);
    }

    public static Optional<UserInfo> checkURLForLoginSuccess(final FlowInfo flowInfo, final String url) throws IOException {
        final URI uri = URI.create(url);

        if (url.startsWith(REDIRECT_URI)) {
            var fragmentParams = getQueryParams(uri);

            final String code = fragmentParams.get("code");
            final String state = fragmentParams.get("state");

            if (flowInfo.getState().equals(state) && !StringUtils.isEmpty(code)) {
                final Auth0OauthResponse tokenInfo;

                try (RESTAuth0Client client = buildAuth0Client(null)) {
                    tokenInfo = client.getOauthToken(CLIENT_ID, code, flowInfo.getVerifier(), REDIRECT_URI);


                    Optional<UserInfo> userInfo = JWTUtils.verifyToken(tokenInfo.getIdToken()).map(jwt -> {
                        return UserInfo.builder()
                                .jwtToken(tokenInfo.getIdToken())
                                .email(jwt.getClaim("email").asString())
                                .name(jwt.getClaim("name").asString())
                                .avatarURL(jwt.getClaim("picture").asString())
                                .build();
//                                .mongodbUsername(jwt.getClaim("mangodbUsername").asString())
//                                .mongodbPassword(jwt.getClaim("mangodbPassword").asString())
                    });

                    userInfo.ifPresent(u -> u.setAuth0OauthResponse(tokenInfo));
                    return userInfo;
                }
            }
        }

        return Optional.empty();
    }

    private static RESTAuth0Client buildAuth0Client(final String accessToken) {
        return new RESTAuth0Client(URI.create("https://" + DOMAIN + "/"), accessToken != null ? new ClientAuthenticationCredentials("Bearer " + accessToken) : null);
    }

    private static Map<String, String> getQueryParams(URI uri) {
        final Map<String, String> fragmentParams = new LinkedHashMap<>();
        final String query = uri.getQuery();
        if (query == null) {
            return fragmentParams;
        }

        final String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            fragmentParams.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8), URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
        }

        return fragmentParams;
    }
}
