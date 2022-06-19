package Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Dao.UserDao;

@Path("/main")
public class ResourceMainPages {
	
	/**
	* GET request at path /main checks which organisation the user belongs to by
	* its accesstoken and shows a different main page for kickin members and 
	* organisation members
	*/
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnPage(@CookieParam("access_token") String access_token) {
		
		String organisation = UserDao.instance.getUser(access_token).getOrganisation();
		
		
		InputStream file = null;
		if (organisation.equals("KickInCommittee")) {
			file = this.getClass().getClassLoader().getResourceAsStream("staticHTML/kickin.html");
		} else {
			file = this.getClass().getClassLoader().getResourceAsStream("staticHTML/organisation.html");
		}
		
		
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new InputStreamReader(file, "UTF-8"));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		        contentBuilder.append(System.getProperty("line.separator"));
		    }
		    in.close();
		} catch (IOException e) {
		}
		
		String content = contentBuilder.toString();
		
		return content;
	}
	
	/**
	* GET request at path /main/email/{emailID} will be succesfull only if the user has access,
	* if succesfull, the emailpage is showed
	*/
	@GET
	@Path("email/{emailID}")
	@Produces(MediaType.TEXT_HTML)
	public String returnPage(@CookieParam("access_token") String access_token, @PathParam("emailID") int id) {

		String email = UserDao.instance.getUser(access_token).getEmail();
		
		InputStream file = null;
		
		Database db = new Database();
		
		String organisation = UserDao.instance.getUser(access_token).getOrganisation();
		
		if (!organisation.equals("KickInCommittee")) {
			boolean permission = db.HasPermissionForEmail(email, id);
			if (!permission) {
				file = this.getClass().getClassLoader().getResourceAsStream("staticHTML/noEmailPermission.html");
			} else {
				
				file = this.getClass().getClassLoader().getResourceAsStream("staticHTML/emailOrganisation.html");
				
			}
		} else {
			
			file = this.getClass().getClassLoader().getResourceAsStream("staticHTML/emailKickIn.html");
		}
		
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new InputStreamReader(file, "UTF-8"));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		        contentBuilder.append(System.getProperty("line.separator"));
		    }
		    in.close();
		} catch (IOException e) {
		}
		
		String content = contentBuilder.toString();
		
		content = content.replaceAll("var emailid = 0;", "var emailid = " +  id + ";");
		
		return content;
	}
	@GET
    @Path("/new")
    @Produces(MediaType.TEXT_HTML)
    public String returnAddEmailPage(@CookieParam("access_token") String access_token,
            @QueryParam("error") String error, @QueryParam("success") String success) {

        String organisation = UserDao.instance.getUser(access_token).getOrganisation();


        InputStream file = null;
        if (organisation.equals("KickInCommittee")) {
            file = this.getClass().getClassLoader().getResourceAsStream("staticHTML/addEmails.html");
        } else {
            file = this.getClass().getClassLoader().getResourceAsStream("staticHTML/noEmailPermission.html");
        }

        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(file, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
                contentBuilder.append(System.getProperty("line.separator"));
            }
            in.close();
        } catch (IOException e) {
        }

        String content = contentBuilder.toString();
        if (success != null) {
            if (!success.isEmpty()) {
                content = content.replaceAll("var success = 0;", "var success = " +  success + ";");
            }
        } else if (error != null) {
            if (!error.isEmpty()) {
                content = content.replaceAll("var error = 0;", "var error = " +  error + ";");
            }
        }

        return content;
    }
	
	
}
