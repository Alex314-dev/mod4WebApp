package Auth;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import Dao.UserDao;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.Provider;


@Provider
public class Filtering implements ContainerRequestFilter {
	
	private String targetURIForRedirection = "../LoginPage.html";
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		String requestPath = requestContext.getUriInfo().getPath();
		
		System.out.print("Path: ");
		System.out.println(requestPath);
		
		String cookie = "";
		//Extract the cookie with name 'access_token'
		for (Cookie c : requestContext.getCookies().values()) 
		{
		    if (c.getName().equals("access_token")) {
		    	System.out.println(c.getValue());
		        cookie = c.getValue();
		        break;
		    }
		}
		
		if (!cookie.isEmpty()) {
			//check if there exists a User with that access_code
			if (UserDao.instance.checkSession(cookie)) {
				//checks if the session is expired
				if (UserDao.instance.getUser(cookie).getSessionExpiery().isAfter(LocalDateTime.now())) {
					//System.out.println(UserDao.instance.getUser(cookie).getEmail());
					//System.out.println(UserDao.instance.getUser(cookie).getName());
					//Return to the corresponding REST method
					//System.out.println(UserDao.instance.getUser(cookie).getSessionExpiery());
					//System.out.println(LocalDateTime.now());
					return;
				} else {
					UserDao.instance.removeUser(cookie);
				}
			}
			
			
		} if (requestPath.equals("auth")) {
			//if the endpoint is for authentication let the client perform the request
			return;
		} else if (requestPath.startsWith("documents")) {
			//REST endpoint for documents has it's own filtering because of the use of google preview
			//instead of cookies the queryParametes should be checked for the correct hashed access token
			MultivaluedMap<String,String> queryParametesrs = requestContext.getUriInfo().getQueryParameters();
			String access_token = queryParametesrs.getFirst("access_token");
			//check if there exists a User with that access_code
			if (UserDao.instance.checkSession(access_token)) {
				if (UserDao.instance.getUser(access_token).getSessionExpiery().isAfter(LocalDateTime.now())) {
					//System.out.println(UserDao.instance.getUser(access_token).getSessionExpiery());
					//System.out.println(LocalDateTime.now());
					return;
				}
				UserDao.instance.removeUser(access_token);
				Response response = Response.noContent().build();
				requestContext.abortWith(response);
				return;
			}
		}
		
		
		Response response = Response.noContent().build();
			
		URI uri = null;
		try {
			uri = new URI(targetURIForRedirection);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		//create a response which redirects to the login page
		response = Response
				.seeOther(uri)
				.build();
		
		requestContext.abortWith(response);
		
	}
	
	
}
