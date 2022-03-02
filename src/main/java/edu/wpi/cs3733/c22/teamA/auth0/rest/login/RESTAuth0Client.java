package edu.wpi.cs3733.c22.teamA.auth0.rest.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wpi.cs3733.c22.teamA.auth0.rest.ApiRESTIOException;
import edu.wpi.cs3733.c22.teamA.auth0.rest.AbstractRESTConsumer;
import edu.wpi.cs3733.c22.teamA.auth0.rest.ClientAuthenticationCredentials;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

import edu.wpi.cs3733.c22.teamA.auth0.utils.Utils;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

public class RESTAuth0Client extends AbstractRESTConsumer {

    public RESTAuth0Client(URI baseURI, ClientAuthenticationCredentials apiKeyAuthorization) {
        super(baseURI, apiKeyAuthorization);
    }

    public Auth0UserInfo getUserInfo() throws IOException {
        return get(baseURI.resolve("/userinfo"), Auth0UserInfo.class);
    }

    public static final MediaType FORM_URL_ENCODED = MediaType.parse("application/x-www-form-urlencoded");

    public Auth0OauthResponse getOauthToken(final String clientId, final String code, final String codeVerifier, final String redirectUri) throws IOException {
        return withRetry(() -> {
            final String data = "grant_type=authorization_code&client_id=" + clientId + "&code_verifier=" + codeVerifier + "&code=" + code + "&redirect_uri=" + redirectUri;
            final RequestBody body = RequestBody.create(data, FORM_URL_ENCODED);

            final Request request = new Request.Builder()
                    .url(baseURI.resolve("/oauth/token").toURL())
                    .post(body)
                    .build();

            try (Response response = getClient().newCall(request).execute()) {
                String response_str = Objects.requireNonNull(response.body()).string();
                System.out.println(response_str);
                return new ObjectMapper()
                        .readerFor(Auth0OauthResponse.class)
                        .readValue(response_str);
            } catch (IOException e) {
                throw new ApiRESTIOException((IOException) e);
            }
        });
    }

//    public void getMetadata(final String clientId, final String code, final String codeVerifier, final String redirectUri) throws IOException {
//        withRetry(() -> {
//            final String data = "grant_type=authorization_code&client_id=" + clientId + "&code_verifier=" + codeVerifier + "&code=" + code + "&redirect_uri=" + redirectUri;
//            final RequestBody body = RequestBody.create(data, FORM_URL_ENCODED);
//
//            final Request request = new Request.Builder()
//                    .url(baseURI.resolve(String.format("/api/v2/users/%s", clientId)).toURL())
//                    .post(body)
//                    .build();
//
//            try (Response response = getClient().newCall(request).execute()) {
//                return new ObjectMapper()
//                        .readerFor(Auth0OauthResponse.class)
//                        .readValue(Objects.requireNonNull(response.body()).string());
//            } catch (IOException e) {
//                throw new ApiRESTIOException((IOException) e);
//            }
//        });
//    }

}
