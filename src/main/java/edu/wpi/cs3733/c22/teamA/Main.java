package edu.wpi.cs3733.c22.teamA;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.AuthRequest;
import com.auth0.net.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import java.sql.SQLException;
import java.util.Objects;

public class Main {

  public static void main(String[] args) {


//      String response_str = "{\"access_token\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiaXNzIjoiaHR0cHM6Ly9kZXYteDdianQ2MmkudXMuYXV0aDAuY29tLyJ9..kiWGo18wyamIFJGe.NKUFfEPKfQ2Ox6M-EYVBf5qiUBqoJ52i0Sx7lpXZum-KIEC8R9HRajBS_OxtIjyPNqr_1dtFAU2UMWgmyR_ajVvLZ1Ie26LEcBFiMVxiMGMxi1DMn71ttcrrXENjfqASsy92WBfxpeQKWl1_CqOxW8yE5J9GnVp3eQ0XsT11lR8jv8zlBxprwTr7Q0XbzusGgwxVlz7jlMRo67Lf_UwqfM1O_V9s4fDPlrFK4ROfnOlrDPPrPVTzcernquEA_otzINF-A2_OIiak9t1Sc1miVVgnaGQ53VdKxmZhZs4YiR6QiWCh6dV4_zgMCXh8wOXlKvs.fpLdZCZSQGRBgxmEOIi_4A\",\"id_token\":\"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InNyMDMtZ3MyT0FScWRBT1Y4VnFJcSJ9.eyJuaWNrbmFtZSI6InlpZGlrdXQiLCJuYW1lIjoieWlkaWt1dEB3cGkuZWR1IiwicGljdHVyZSI6Imh0dHBzOi8vcy5ncmF2YXRhci5jb20vYXZhdGFyLzVkMTJjYWFjMzM4ZGM3NmZjNzc4MWQ5Y2UyYzlmYTg0P3M9NDgwJnI9cGcmZD1odHRwcyUzQSUyRiUyRmNkbi5hdXRoMC5jb20lMkZhdmF0YXJzJTJGeWkucG5nIiwidXBkYXRlZF9hdCI6IjIwMjItMDItMjhUMTY6Mzk6MDguNjk5WiIsImVtYWlsIjoieWlkaWt1dEB3cGkuZWR1IiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImlzcyI6Imh0dHBzOi8vZGV2LXg3Ymp0NjJpLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJhdXRoMHw2MjFjNmRjZDQxZjJjYzAwNjllNmQ0NjciLCJhdWQiOiIxdlUza3JVbkVON2ljYUU0RUhUOGxFdFFMRmZ6eVIwWSIsImlhdCI6MTY0NjA2NjM1MCwiZXhwIjoxNjQ2MTAyMzUwfQ.nwtTNkJZq--Kmnp90JJfDBiRMIzZU8azjDzIQ2JtQACOAlbU98cEWd-NT9__Gb-ukn37FqCPdxUdr8SG9GkHsFJ1oXluWHR4YHwW4CrIXOQrtYwqGFWHYJk409uTYbVhjaw8jeI_8LJl5GUhuItCmDRU8dBhGMXACVXPNVhbZfJtA7KsRUlHgST3k2t-lfYWFa4yKae53sxNiANqV3qgYpgJ0DCHMWI4dev9gemEFbHylFbgXmN3bcw8izu1yZbEMgySwNFYWCuCg-pRRNIPYBmbitCFJI6c-Nk85Tr-U84P2PqmNx-pWAPrdeLGuld1zGwtilGF9VruUBmTFvx4gA\",\"scope\":\"openid profile email\",\"expires_in\":86400,\"token_type\":\"Bearer\"}";
//
//      try
//      {
//        Auth0OauthResponse a = new ObjectMapper()
//                .readerFor(Auth0OauthResponse.class)
//                .readValue(response_str);
//        System.out.println(a);
//      } catch (Exception ignored) {}

    try {
      AuthAPI authAPI = new AuthAPI(Auth0PKCEFlow.DOMAIN, Auth0PKCEFlow.CLIENT_ID, Auth0PKCEFlow.CLIENT_SECRET);
      AuthRequest authRequest = authAPI.requestToken("https://" + Auth0PKCEFlow.DOMAIN + "/api/v2/");
      TokenHolder holder = authRequest.execute();
      ManagementAPI mgmt = new ManagementAPI(Auth0PKCEFlow.DOMAIN, holder.getAccessToken());

      Request<User> request = mgmt.users().get("auth0|621c6dcd41f2cc0069e6d467", new UserFilter());

      User response = request.execute();
      for (String key: response.getAppMetadata().keySet()) {
        System.out.printf("metadata Key: %s\tValue: %s\n", key, response.getAppMetadata().get(key));
      }
    } catch (APIException exception) {
      // api error
    } catch (Auth0Exception exception) {
      // request error
    }

//    App.launch(App.class, args);
  }
}
