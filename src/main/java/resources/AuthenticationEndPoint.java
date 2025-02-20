package resources;

import entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Path("/authenticate")
public class AuthenticationEndPoint {

    @Context
    private UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticateUser(@FormParam("username") String username ,@FormParam("pwd") String pwd){
        authentification(username, pwd);
        String jwt = generateToken((username));
        return Response.ok().entity(jwt).build();

    }

    public String  generateToken(String username){
        String keyString = "simplekey";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
        System.out.println("the key is : " + key.hashCode());
        System.out.println("uriInfo.getAbsolutePath().toString() : " + uriInfo.getAbsolutePath().toString());
        System.out.println("Expiration date: " + toDate(LocalDateTime.now().plusMinutes(15L)));
        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        System.out.println("the returned token is : " + jwtToken);
        return jwtToken;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public void authentification(String username, String  pwd){
        System.out.println("Authentification Pending ...");
    }

}
