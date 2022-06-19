package Resource;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import Dao.UserDao;
import Model.User;

@Path("userInfo")
public class ResourceUserInfo {
	
	/**
	* This GET request returns a user in JSON from the accessToken
	*/
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserInfo (@CookieParam("access_token") String access_token) {
		
		User user = UserDao.instance.getUser(access_token);
		
		return user;
	}
	
	
}
