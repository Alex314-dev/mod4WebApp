package Auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

import Model.User;
import Resource.Database;
import Dao.UserDao;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;

@Path("/auth")
public class Authorize {
	
	@Context UriInfo uri;
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getCode(@FormParam("code") String code,
			Form form, @Context HttpHeaders httpheaders) throws IOException, InterruptedException {
		
		URI reddirectUri = null;
		
		if (code == null || code.isEmpty()) {
			
			try {
				reddirectUri = new URI("../LoginPage.html");
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Response response = Response.seeOther(reddirectUri)
					.build();
			return response;
		}
		
		Callback callback = new Callback();
		String host = uri.getBaseUri().toString();
		String responseJson = callback.retriveTokens(code, host);
		
		if (responseJson.equals("error")) {
			
			try {
				reddirectUri = new URI("../LoginPage.html");
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Response response = Response.seeOther(reddirectUri)
					.build();
			return response;
			
		}
		
		
		Gson gson = new Gson();
		
		Tokens tokens = gson.fromJson(responseJson, Tokens.class); 
		
		String plainAccessToken = tokens.getAccess_token();
		
		String hashedToken = hashAccessToken(plainAccessToken);
		
		if (hashedToken.isEmpty() || hashedToken == null) {
			Response response = Response.seeOther(reddirectUri)
					.build();
			return response;
		}
		
		tokens.setAccess_token(hashedToken);
		
		storeUser(tokens);
		
		String access_token = tokens.getAccess_token();
		
		
		try {
			reddirectUri = new URI("main");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		Response response = Response.seeOther(reddirectUri)
				.entity(null).cookie(new NewCookie("access_token", access_token))
				.build();
		
		return response;
		
	}
	
	private String hashAccessToken(String access_token) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			
			byte[] messageDigest = md.digest(access_token.getBytes());
			
			BigInteger no = new BigInteger(1, messageDigest);
			
			String hashtext = no.toString(16);
			
			while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
			
			return hashtext;
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	//store user's session
	private void storeUser(Tokens tokens) {
		
		String encodedPayload = "";
		try {
			encodedPayload = new String(tokens.getId_token().split("\\.")[1].getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//this should't happen but just in case
		if (encodedPayload.isEmpty()) {
			return;
		}
		
		String decodedPayload = new String(Base64.getUrlDecoder().decode(encodedPayload));
		
		//System.out.println(decodedPayload);
		
		Gson gson = new Gson();
		User user = gson.fromJson(decodedPayload, User.class);
		
		//sets an expiery for 60 minutes
		user.setSessionExpiery(LocalDateTime.now().plusMinutes(60));
		
		//store Id token for the log-out functionality
		user.setId_token(tokens.getId_token());
		
		String email = user.getEmail();
		
		Database db = new Database ();
		//with the corresponding email get the Users organisation
		String organisation  = db.getUserOrganisation(email);
		
		if (organisation == null) {
			return;
		}
		
		user.setOrganisation(organisation);
		
		String access_token = tokens.getAccess_token();
		
		
		if (UserDao.instance.checkForExistingEmail(email)) {
			
			String oldAccessToken = UserDao.instance.getInverseUser(email);
			UserDao.instance.removeUser(oldAccessToken);
		}
		
		UserDao.instance.addUser(access_token, user);
		UserDao.instance.addInverseUser(email, access_token);
		
	}
	
}
