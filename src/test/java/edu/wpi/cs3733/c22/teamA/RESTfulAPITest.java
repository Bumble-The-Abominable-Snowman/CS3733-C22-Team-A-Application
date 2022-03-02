package edu.wpi.cs3733.c22.teamA;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import lombok.val;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class RESTfulAPITest {
    @Test
    public void privateAuthTest() throws IOException, ParseException, SQLException, InvocationTargetException, IllegalAccessException {

//        val jsonObject = JSONObject()
//        jsonObject.put("name", "Ancd test")
//        jsonObject.put("city", "delhi")
//        jsonObject.put("age", "23")
//        val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        final MediaType JSON = MediaType.get("application/json; charset=utf-8");


        Map<String, String> map = new HashMap<>();
        map.put("operation", "read");
        map.put("operation2", "read2");

        System.out.println(String.valueOf(new JSONObject(map)));
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), String.valueOf(new JSONObject(map)));

        Request request = new Request.Builder()
                .url("https://cs3733c22teama.ddns.net/api/locations")
                .addHeader("Content-Type", "application/json")
                .addHeader("authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InNyMDMtZ3MyT0FScWRBT1Y4VnFJcSJ9.eyJpc3MiOiJodHRwczovL2Rldi14N2JqdDYyaS51cy5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NjIxYzZkY2Q0MWYyY2MwMDY5ZTZkNDY3IiwiYXVkIjpbImh0dHBzOi8vY3MzNzMzYzIydGVhbWEuZGRucy5uZXQvYXBpLyIsImh0dHBzOi8vZGV2LXg3Ymp0NjJpLnVzLmF1dGgwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE2NDYyMzYwNjcsImV4cCI6MTY0NjMyMjQ2NywiYXpwIjoiMXZVM2tyVW5FTjdpY2FFNEVIVDhsRXRRTEZmenlSMFkiLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwicGVybWlzc2lvbnMiOlsicmVhZDpkYi1lbXBsb3llZS1hZG1pbiIsInJlYWQ6ZGItZW1wbG95ZWUtYWlkZSIsInJlYWQ6ZGItZW1wbG95ZWUtY291cmllciIsInJlYWQ6ZGItZW1wbG95ZWUtY3VzdG9kaWFuIiwicmVhZDpkYi1lbXBsb3llZS1kb2N0b3IiLCJyZWFkOmRiLWVtcGxveWVlLWZvb2QiLCJyZWFkOmRiLWVtcGxveWVlLWxhbmd1YWdlIiwicmVhZDpkYi1lbXBsb3llZS1sYXVuZHJ5IiwicmVhZDpkYi1lbXBsb3llZS1tYWludGVuYW5jZSIsInJlYWQ6ZGItZW1wbG95ZWUtbnVyc2UiLCJyZWFkOmRiLWVtcGxveWVlLXJlbGlnaW91cyIsInJlYWQ6ZGItZW1wbG95ZWUtc2VjdXJpdHkiLCJyZWFkOmRiLWVtcGxveWVlLXN0YWZmIiwicmVhZDpkYi1lcXVpcG1lbnQiLCJyZWFkOmRiLWxvY2F0aW9ucyIsInJlYWQ6ZGItbWVkaWNpbmUiLCJyZWFkOnNyLWNvbnN1bHRhdGlvbiIsInJlYWQ6c3ItZXF1aXBtZW50LWRlbGl2ZXJ5IiwicmVhZDpzci1mbG9yYWwtZGVsaXZlcnkiLCJyZWFkOnNyLWZvb2QtZGVsaXZlcnkiLCJyZWFkOnNyLWdpZnQtZGVsaXZlcnkiLCJyZWFkOnNyLWxhbmd1YWdlIiwicmVhZDpzci1sYXVuZHJ5IiwicmVhZDpzci1tYWludGVuYW5jZSIsInJlYWQ6c3ItbWVkaWNpbmUtZGVsaXZlcnkiLCJyZWFkOnNyLXJlbGlnaW91cyIsInJlYWQ6c3Itc2FuaXRhdGlvbiIsInJlYWQ6c3Itc2VjdXJpdHkiLCJ1c2VyLWFkbWluIiwidXNlci1haWRlIiwidXNlci1jb3VyaWVyIiwidXNlci1jdXN0b2RpYW4iLCJ1c2VyLWRvY3RvciIsInVzZXItZm9vZCIsInVzZXItbGFuZ3VhZ2UiLCJ1c2VyLWxhdW5kcnkiLCJ1c2VyLW1haW50ZW5hbmNlIiwidXNlci1udXJzZSIsInVzZXItcmVsaWdpb3VzIiwidXNlci1zZWN1cml0eSIsInVzZXItc3RhZmYiLCJ3cml0ZTpkYi1lbXBsb3llZSIsIndyaXRlOmRiLWVxdWlwbWVudCIsIndyaXRlOmRiLWxvY2F0aW9ucyIsIndyaXRlOmRiLW1lZGljaW5lIiwid3JpdGU6c3ItY29uc3VsdGF0aW9uIiwid3JpdGU6c3ItZXF1aXBtZW50LWRlbGl2ZXJ5Iiwid3JpdGU6c3ItZmxvcmFsLWRlbGl2ZXJ5Iiwid3JpdGU6c3ItZm9vZC1kZWxpdmVyeSIsIndyaXRlOnNyLWdpZnQtZGVsaXZlcnkiLCJ3cml0ZTpzci1sYW5ndWFnZSIsIndyaXRlOnNyLWxhdW5kcnkiLCJ3cml0ZTpzci1tYWludGVuYW5jZSIsIndyaXRlOnNyLW1lZGljaW5lLWRlbGl2ZXJ5Iiwid3JpdGU6c3ItcmVsaWdpb3VzIiwid3JpdGU6c3Itc2FuaXRhdGlvbiIsIndyaXRlOnNyLXNlY3VyaXR5Il19.sRAwVq1DxVVMfJJEwPsRElTPoFKLS07ZdiPitgv82oQx4Aktoomtd_yZoV6m1G2AOs3F5Id6tKzsEPDYEtbA17HiRXJ63-3ev4RW6J4fvMuLBQ-5i6r6OkZRVj_EnrNrkPYBr0FjJDN0-oGrPkFvnOgyHP7hJTxOE_KLbJy_RUiJYBajjdzlKpM1aLIA42XxQnTcyYP-Z-xhWJu1jQUbSCvWkskzRLhfrWv3bMPjtjWc3YAWcAQKbZuGMAiXNX15eBHSlyPIEw--T2udeWzf_vj3SzZbggn6l45metwFQLjkmFQrtTspO3Fz7QTlValmztkM1fHbH_ZJXhkQPxIJ_w")
                .post(body)
                .build();


        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        Response response = call.execute();
//        response.close();

        System.out.println(response.code());
        System.out.println(response.message());
        System.out.println(Objects.requireNonNull(response.body()).string());

    }
}
