package Resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import Dao.EmailDao;
import Dao.UserDao;
import Model.Ajax;
import Model.Email;
import Model.PersonReadDoc;
import Model.PersonReadEmail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Path("/emails")
public class ResourceEmail {
	
	@GET
	@Path("content/{itemID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Email returnEmail(@CookieParam("access_token") String access_token, @PathParam("itemID") int id) {
		
		String organisation = UserDao.instance.getUser(access_token).getOrganisation();
		String email = UserDao.instance.getUser(access_token).getEmail();
		
		Database db = new Database();
		
		if (!organisation.equals("KickInCommittee")) {
			boolean permission = db.HasPermissionForEmail(email, id);
			if (!permission) {
				return null;
			} else {
				db.markReadEmail(id, email);
				
			}
		}
		
		Database database = new Database();
		
		return database.getEmailById(id);
		
	}
	
	@GET
	@Path("organisations")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> returnOrganisations (@CookieParam("access_token") String access_token) {
		if (UserDao.instance.getUser(access_token).getOrganisation().equals("KickInCommittee")) {
			
			Database database = new Database();
			return database.allOrganisations();
			
		}
		
		return null;
	}
	
	@GET
	@Path("people")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> returnPeople (@CookieParam("access_token") String access_token) {
		if (UserDao.instance.getUser(access_token).getOrganisation().equals("KickInCommittee")) {
			
			Database database = new Database();
			return database.allPeople();
			
		}
		
		return null;
	}
	
	@GET
	@Path("documents")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> returnDocuments (@CookieParam("access_token") String access_token) {
		if (UserDao.instance.getUser(access_token).getOrganisation().equals("KickInCommittee")) {
			
			Database database = new Database();
			return database.allDocuments();
			
		}
		
		return null;
	}
	
	
	@GET
	@Path("kickincomittie")
	@Produces(MediaType.APPLICATION_JSON)
	public Ajax getEmailBrowserKickIn(@QueryParam("draw") int draw,
			@QueryParam("search") String search,
			@QueryParam("start") int start,
			@QueryParam("length") int length,
			@QueryParam("dateOrder") String dateOrder,
			@QueryParam("organisation") String organisation,
			@QueryParam("senderEmail") String senderEmail,
			@QueryParam("receiverEmail") String receiverEmail,
			@QueryParam("hasAttachment") boolean hasAttachment,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate)
		{
		
		Database database = new Database();
		
		int recordsTotal = database.countEmailsKickInMember(); //for kick in member
		
		Object[] result = database.searchByKeywordKickInMember(
			search, dateOrder, startDate, endDate, organisation, senderEmail, receiverEmail, length, start, hasAttachment); //for kick in member

		int filteredRecords = (int) result[1];
		EmailDao resultEmails = (EmailDao) result[0];
		
		
		List<Email> emails = new ArrayList<Email>();
		emails.addAll(resultEmails.getData().values());
		
		if (dateOrder.equals("asc")) {
			Collections.sort(emails, 
	                (email2, email1) -> email2.getSendAt().compareTo(email1.getSendAt()));
		} else if (dateOrder.equals("desc")) {
			Collections.sort(emails, 
	                (email1, email2) -> email2.getSendAt().compareTo(email1.getSendAt()));	
		} else {
			return null;
		}
		
		Ajax ajax = new Ajax(emails, draw, recordsTotal, filteredRecords);
		
		return ajax;
	}
	
	@GET
	@Path("organisationmember")
	@Produces(MediaType.APPLICATION_JSON)
	public Ajax getEmailBrowserOrganisationMember(
			@CookieParam("access_token") String access_code,
			@QueryParam("draw") int draw,
			@QueryParam("search") String search,
			@QueryParam("start") int start,
			@QueryParam("length") int length,
			@QueryParam("dateOrder") String dateOrder,
			@QueryParam("senderEmail") String senderEmail,
			@QueryParam("receiverEmail") String receiverEmail,
			@QueryParam("hasAttachment") boolean hasAttachment,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate)
		{
		
		Database database = new Database();
		
		String emailAddress = UserDao.instance.getUser(access_code).getEmail();
		
		int recordsTotal = database.countEmailsAssociationMember(emailAddress); //for association member
		
		Object[] result = database.searchByKeywordAssociationMember(emailAddress,
			search, dateOrder, startDate, endDate, senderEmail, receiverEmail, length, start, hasAttachment);

		
		int filteredRecords = (int) result[1];
		EmailDao resultEmails = (EmailDao) result[0];
		
		
		List<Email> emails = new ArrayList<Email>();
		emails.addAll(resultEmails.getData().values());
		
		if (dateOrder.equals("asc")) {
			Collections.sort(emails, 
	                (email2, email1) -> email2.getSendAt().compareTo(email1.getSendAt()));
		} else if (dateOrder.equals("desc")) {
			Collections.sort(emails, 
	                (email1, email2) -> email2.getSendAt().compareTo(email1.getSendAt()));	
		} else {
			return null;
		}
		
		Ajax ajax = new Ajax(emails, draw, recordsTotal, filteredRecords);
		
		return ajax;
	}
	
	
	@GET
	@Path("readcontent/{itemID}")
	@Produces(MediaType.APPLICATION_JSON)
	public PersonReadEmail[] getReadEmails (@CookieParam("access_token") String access_token, @PathParam("itemID") int id) {
		if (UserDao.instance.getUser(access_token).getOrganisation().equals("KickInCommittee")) {
			
			Database db = new Database();
			
			PersonReadEmail[] readEmail = db.getReadEmail(id);
			if (readEmail == null) {
				
				PersonReadEmail empty = new PersonReadEmail("no data", "");
				readEmail = new PersonReadEmail[1];
				readEmail[0] = empty;
				return readEmail;
			}
			return readEmail;
			
			
		}
		
		return null;
	}
	
	
}
