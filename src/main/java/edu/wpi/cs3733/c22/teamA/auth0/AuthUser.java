package edu.wpi.cs3733.c22.teamA.auth0;

import com.auth0.jwk.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeWrapperImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Employee;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AuthUser {

    private String token = "";
    private String email;
    private ArrayList<String> permissions = new ArrayList<>();

    private Employee employee;

    public AuthUser() {

    }

    public AuthUser(String params) {
        this.token = params.split("&")[0];
        this.email = URLDecoder.decode(params.split("&")[1]);
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
    }


    public String getToken()
    {
        return this.token;
    }

    public Employee getEmployee() {return this.employee;}
    public void setEmployee(Employee e) {this.employee = e;}

    public String getEmployeeName() throws IOException, ParseException {

        if (!App.DB_CHOICE.equals("nosql"))
        {
            return "Admin (admin)";
        }

        List<Employee> employeeArrayList = (new EmployeeWrapperImpl()).getEmployeeList();
        for (Employee emp: employeeArrayList) {
            if (this.email.equals(String.format("email=%s", emp.getStringFields().get("email"))))
            {
                this.employee = emp;
            }
        }
        if (this.employee != null)
        {
            return this.employee.getFullName();
        }
        else {
            return "EMPLOYEE NOT FOUND!";
        }
    }

    public ArrayList<String> getPermissions() {return this.permissions;}



}
