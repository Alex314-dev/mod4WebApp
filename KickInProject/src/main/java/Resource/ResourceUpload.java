package Resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("upload")
public class ResourceUpload {
	
	@POST
	@Path("/email")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newEmail(@FormParam("receiver") String receiver,
						@FormParam("subject") String subject,
						@FormParam("sender") String sender,
						@FormParam("senderEmail") String senderEmail,
						@FormParam("content") String content,
						@FormParam("attachment[]") List<String> attachments,
						@FormParam("date") String datetime) {
							
		Database db = new Database();		
		String targetURIForRedirection = "main/new";
		
		try {
			if (receiver != null && datetime != null) {
				
				if (!receiver.isEmpty() && !datetime.isEmpty() ) {
					
					String date = datetime.split("T")[0];
					String time = datetime.split("T")[1] + ":00";
					System.out.println(date+" "+time);
					
					if (receiver.contains("@")) {
					
						String email = receiver.split("\\(")[1].split("\\)")[0];
						if (!db.checkExistPerson(email)) {
							targetURIForRedirection = "main/new?error='Receiver%20does%20not%20exists'";
						} else { //if receiver is person
						
							db.addNewCommunication(receiver, subject, sender, senderEmail, content, (date+" "+time), attachments);
							targetURIForRedirection = "main/new?success='done'";
						}
					} else {
						if (!db.checkExistOrganisation(receiver)) {
							targetURIForRedirection = "main/new?error='Receiver%20does%20not%20exists'";
						} else { //if receiver is organisation
							db.addNewCommunication(receiver, subject, sender, senderEmail, content, (date+" "+time), attachments);
							targetURIForRedirection = "main/new?success='done'";
						}
					}
				} else {
					targetURIForRedirection = "main/new?error='Missing%20fields'";
				}
			} else {
				targetURIForRedirection = "main/new?error='Missing%20fields'";
			}
		} catch(Exception e) {
			System.err.println("Error: " + e);
			targetURIForRedirection = "main/new?error='Something%20went%20wrong'";
		}
		
		
		URI uri = null;
		try {
			uri = new URI(targetURIForRedirection);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		//create a response which redirects to the login page
		Response response = Response
				.seeOther(uri)
				.build();
		
		return response;
	}
	
	
	@POST
	@Path("/person")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newPerson(@FormParam("organisation") String organisation,
						@FormParam("name") String name,
						@FormParam("email") String email) {
		
		Response response = null;
		String targetURIForRedirection = "main/new";
		
		Database db = new Database();
		
		if (organisation != null) {
			if (organisation.isEmpty()) {
				targetURIForRedirection = "main/new?error='Organisation%20cannot%20be%20empty'";
			} else if (organisation.length() < 5) {
				System.out.println(organisation.length());
				targetURIForRedirection = "main/new?error='Organisation%20cannot%20be%20less%20than%205%20characters'";
			} else if (!db.checkExistOrganisation(organisation)) {
				targetURIForRedirection = "main/new?error='Organisation%20does%20not%20exist'";
			} else if (db.checkExistPerson(email)) {
				targetURIForRedirection = "main/new?error='Email%20already%20exists'";
			} else {
				try {
					db.addNewPerson(organisation, name, email);
					targetURIForRedirection = "main/new?success='done'";
				} catch (Exception e1) {
					System.err.println("Error: " + e1);
					targetURIForRedirection = "main/new?error='something%20went%20wrong'";
				}
				
			}
		}
		
		URI uri = null;
		try {
			uri = new URI(targetURIForRedirection);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		response = Response
				.seeOther(uri)
				.build();
		
		return response;
	}
	
	
	@POST
	@Path("/organisation")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newOrganisation(@FormParam("organisation") String organisation) {
		
		Response response = null;
		String targetURIForRedirection = "main/new";
		
		Database db = new Database();
		
		if (organisation != null) {
			if (organisation.isEmpty()) {
				targetURIForRedirection = "main/new?error='Organisation%20cannot%20be%20empty'";
			} else if (organisation.length() < 5) {
				System.out.println(organisation.length());
				targetURIForRedirection = "main/new?error='Organisation%20cannot%20be%20less%20than%205%20characters'";
			} else if (db.checkExistOrganisation(organisation)) {
				targetURIForRedirection = "main/new?error='Organisation%20already%20exists'";
			} else {
				try {
					db.addNewOrganisation(organisation);
					targetURIForRedirection = "main/new?success='done'";
				} catch (Exception e1) {
					System.err.println("Error: " + e1);
					targetURIForRedirection = "main/new?error='something%20went%20wrong'";
				}
				
			}
		}
		URI uri = null;
		try {
			uri = new URI(targetURIForRedirection);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		response = Response
				.seeOther(uri)
				.build();
		
		return response;
		
		
	}
	
}
