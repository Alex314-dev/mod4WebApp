package Resource;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import Dao.UserDao;
import Model.PersonReadDoc;


@Path("/documents")
public class ResourceDocument {

	
	@GET
	@Path("/{document}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response  returnOrganisations (@QueryParam("access_token") String access_token, @PathParam("document") String document) {
		{
			
			if (!UserDao.instance.checkSession(access_token)) {
				return Response.status(401).entity("Authorization Required").build();
			}
			
			String userEmail = UserDao.instance.getUser(access_token).getEmail();
			String userOrganisation = UserDao.instance.getUser(access_token).getOrganisation();
			
			if (!userOrganisation.equals("KickInCommittee")) {
				Database db = new Database();
				if (!db.HasPermissionForDocument(userEmail, document)) {
					
					return Response.status(401).entity("Authorization Required").build();
					
				} else {
					
					int doc_id = db.getDocumentID(document);
					db.markReadDoc(doc_id, userEmail);
					
				}
			}
			
			URL res = getClass().getClassLoader().getResource("documents/" + document);
			
	        StreamingOutput fileStream =  new StreamingOutput()
	        {
	            @Override
	            public void write(java.io.OutputStream output) throws IOException, WebApplicationException 
	            {
	                try
	                {
	                    java.nio.file.Path path = Paths.get(res.toURI());
	                    byte[] data = Files.readAllBytes(path);
	                    output.write(data);
	                    output.flush();
	                } 
	                catch (Exception e) 
	                {
	                    throw new WebApplicationException("File Not Found!");
	                }
	            }
	        };
	        return Response
	                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
	                .header("content-disposition","attachment; filename = " + document)
	                .build();
	    }
	}
	
	
	@GET
	@Path("readdoc/{document}")
	@Produces(MediaType.APPLICATION_JSON)
	public PersonReadDoc[] getReadEmails (@CookieParam("access_token") String access_token,
			@PathParam("document") String document) {
		if (UserDao.instance.checkSession(access_token)) {
			if (UserDao.instance.getUser(access_token).getOrganisation().equals("KickInCommittee")) {
				
				Database db = new Database();
				
				int doc_id = db.getDocumentID(document);
				
				PersonReadDoc[] readDoc = db.getReadDoc(doc_id);
				if (readDoc == null) {
					
					PersonReadDoc empty = new PersonReadDoc("no data", "");
					readDoc = new PersonReadDoc[1];
					readDoc[0] = empty;
					return readDoc;
				}
				return readDoc;
			}
		}
		
		return null;
	}
	
}
