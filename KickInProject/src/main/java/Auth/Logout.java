package Auth;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import Dao.UserDao;

@Path("/logout")
public class Logout {

	private static String oktaUrl = "https://dev-63893740.okta.com/oauth2/default/v1/logout";
	
	@Context UriInfo uri;
	
	@GET
	@Produces({ MediaType.TEXT_PLAIN })
	public Response getReadEmails (@CookieParam("access_token") String access_token) {
			
		//needed for log out from okta
		String id_token = UserDao.instance.getUser(access_token).getId_token();
		
		System.out.println(id_token);
		
		URI restUri = uri.getBaseUri();
		String reddirectUri = restUri.toString().replace("rest/", "LoginPage.html");
		
		String parameter = "?id_token_hint="+id_token+
				"&"+"post_logout_redirect_uri="+reddirectUri;
		
		Response response = Response.noContent().build();
		
		
		URI uri = null;
		try {
			uri = new URI(oktaUrl+parameter);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		//create a response which redirects to the login page
		response = Response
				.seeOther(uri)
				.build();
		
		return response;
	}
	
	
}
