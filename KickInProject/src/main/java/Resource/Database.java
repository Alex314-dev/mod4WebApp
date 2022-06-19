package Resource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import Dao.EmailDao;
import Model.Attachment;
import Model.Email;
import Model.PersonReadDoc;
import Model.PersonReadEmail;


public class Database {
	//information for methods to access database
    static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String host = "bronto.ewi.utwente.nl";
	static final String dbName = "dab_di20212b_100";
	static final String DB_URL = "jdbc:postgresql://" + host + ":5432/" + 
									dbName +"?currentSchema=kickindatabase";
	
	static final String USER = "dab_di20212b_100";
	static final String PASS = "Txc5x85GyM/DPALd";
	
    
	/**
	 * Counts all the emails the Kick-In member can view
	 * @return integer that is the total of all email records
	 */
    public int countEmailsKickInMember() {

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);

	    	String queryEmailsUser = " SELECT COUNT(c.id)"
	    	    	+ " FROM communication c";
	    		
	    	Statement statementEmailsUser = connection.createStatement();
	    	ResultSet resultSetEmailsUser = statementEmailsUser.executeQuery(queryEmailsUser);
	    	
	    	int recordsTotal = 0;
	    	
	    	while(resultSetEmailsUser.next()) {
	    		recordsTotal = resultSetEmailsUser.getInt(1);
	    	
       
			}
	    	statementEmailsUser.close();
			connection.close();
			
			return recordsTotal;
	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return 0;
		}
    
    }
    
    
	/**
     * Counts all the emails meant for the user or their organisation.
     * @param email - email address to retrieve user's person id
     * @return integer that is the total of all email records accesable by the user
     */
    public int countEmailsAssociationMember(String email) {

    	String userEmail = email;
    	int userId = getUserId(userEmail);
	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);

	    	String queryEmailsUser = " SELECT COUNT(DISTINCT c.id)"
	    	+ " FROM communication c, organisation o, organisationperson op"
	    	+ " WHERE ("
	    	+ "    (c.person_id = " + userId + " AND c.organisation_id = 0)"
	    	+ " OR (c.person_id = 0 "
					+ " AND op.person_id = " + userId + " "
					+ " AND op.organisation_id = o.id)"
	    	+ " OR (c.sender = o.name)"
	    	+ " OR (c.senderemail ='" + userEmail + "') "
	    	+ " )"
	    	
            + " AND o.id = c.organisation_id";
	    	
	    	
	    	Statement statementEmailsUser = connection.createStatement();
	    	ResultSet resultSetEmailsUser = statementEmailsUser.executeQuery(queryEmailsUser);
	    	
	    	int recordsTotal = 0;
	    	
	    	while(resultSetEmailsUser.next()) {
	    		recordsTotal = resultSetEmailsUser.getInt(1);
               
			}
	    	statementEmailsUser.close();
			connection.close();
			
			return recordsTotal;
	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return 0;
		}
    
    }
    
	/**
     * Filters the emails available to kickIn member based on their input. 
	 * The parameters contain the search conditions placed by the user. 
	 * They also contain conditions that limit the results shown by pages 
	 * and assign an order to how the emails are represented (asc/desc dates).
	 * 
     * @param keyWord - the search term
     * @param dateOrder - indicates if emails are shown in ascending or descending order based on date
     * @param startDate - filters to only show users emails from the given date onwards
     * @param endDate - filters to only show users emails before the given date
     * @param organisation - for when emails are send based on organisation 
     * @param senderEmail - for when emails are send based on the individual
     * @param receiverEmail - for when emails are send based on the individual
     * @param length - limits amount of records processed
     * @param start - starting position for which emails to get
     * @param hasAttachment - boolean to check if email has an attachment
     * @return An object array containing the resulting email objects
     */
    public Object[] searchByKeywordKickInMember(String keyWord, String dateOrder, String startDate, String endDate, String organisation, 
											String senderEmail, String receiverEmail, int length, int start, boolean hasAttachment) {

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);
	    	
	    	
			//reduce all spaces to one space
			//remove spaces in front of the keyword
	    	keyWord = keyWord.replaceAll("\\s+", " ");
	    	if (keyWord.startsWith(" ")) {
	    		keyWord = keyWord.substring(1);
	    	}
			String[] queryWords = keyWord.split("[ .,!;:-]+");
			String joinKeyWords = String.join("&", queryWords); 
			
			String senderEmailQuery = "";
			String receiverEmailQuery = "";
			String keyWordQuery = "";				
			String organisationQuery = "";
			String startDateQuery = "";
			String endDateQuery = "";
			String hasAttachmentQuery = "";
			
			
			if (!receiverEmail.isEmpty()) {
				receiverEmailQuery  = " AND p.email = ?";
			}
			
			if (!keyWord.isEmpty()) {
				keyWordQuery = " AND to_tsvector(c.subject||' '||c.content||' '||d.content||' '||d.title||' '||d.type)"
						+ "@@ to_tsquery('english',?)";
			}
			
			if (!organisation.isEmpty()) {
				organisationQuery = " AND o.name = ?";
			}
			
			if (!senderEmail.isEmpty()) {
				senderEmailQuery = " AND c.senderEmail = ?";
			}
			
			if (!startDate.isEmpty()) {
				startDateQuery = " AND c.send_at >= ?";
			}
			
			if (!endDate.isEmpty()) {
				endDateQuery = " AND c.send_at <= ?";
			}
			
			if(hasAttachment) {
				hasAttachmentQuery = " AND d.document_id != 0";
			}
			 
			
			//search for keyword in the email's title, content or attachments
			String query = " SELECT c.id, c.event_id, c.person_id,"
            + " c.organisation_id, c.senderemail, c.subject, c.content,"
            + " c.priority, c.send_at, c.sender, o.name, "
            + " jsonb_agg(jsonb_build_object('id', d.document_id, 'title', d.title, 'type', d.type) ) as attachments"
	    	+ " FROM communication c, organisation o, document_email de, documents d, person p"
	    	+ " WHERE o.id = c.organisation_id "
            + " AND de.communication_id = c.id "
            + " AND de.document_id = d.document_id"
            + " AND p.id = c.person_id"
	    	+ keyWordQuery
	    	+ organisationQuery
			+ senderEmailQuery
			+ receiverEmailQuery
			+ startDateQuery
			+ endDateQuery
			+ hasAttachmentQuery
            + " GROUP BY c.id, c.event_id, c.person_id, c.organisation_id, "
            + " c.senderemail, c.subject, c.content, c.priority, "
            + " c.send_at, c.sender, o.name "
	    	+ " ORDER BY c.send_at " + dateOrder
	    	+ " LIMIT " +  length + " OFFSET " + start + ";";
			
			//count the amount of emails that will be returned
			String count = "SELECT COUNT(DISTINCT c.id),"
		    + " jsonb_agg(jsonb_build_array(d.document_id) ) "
			+ " FROM communication c, organisation o, document_email de, documents d, person p"
			+ " WHERE o.id = c.organisation_id"
            + " AND de.communication_id = c.id"
            + " AND de.document_id = d.document_id"
            + " AND p.id = c.person_id"
	    	+ keyWordQuery
	    	+ organisationQuery
			+ senderEmailQuery
			+ receiverEmailQuery
			+ startDateQuery
			+ endDateQuery
			+ hasAttachmentQuery;
	    	
			
	    	PreparedStatement statement = connection.prepareStatement(query);
	    	
			prepareStatement(statement, joinKeyWords, organisation, senderEmail, receiverEmail, startDate, endDate);

	    	ResultSet resultSet = statement.executeQuery();
	    	
	    	EmailDao emailDao = new EmailDao();
			//display after query: sender@email, sender, title, preview content, date (show if attach? (later))
	    	while(resultSet.next()) {
				Email newEmail = addEmail(resultSet);
				
				emailDao.addEmail(newEmail.getId(), newEmail);	
		    }
	
			statement.close();

			statement = connection.prepareStatement(count);
		
			prepareStatement(statement, joinKeyWords, organisation, senderEmail, receiverEmail, startDate, endDate);
	    	
			resultSet = statement.executeQuery();
			
	    	int countEntries = 0;
	    	
	    	while(resultSet.next()) {
	    		countEntries = resultSet.getInt(1);
	    	}
	    	
	    	statement.close();
			connection.close();
			
			Object[] result = new Object[2];
			result[0] = emailDao;
			result[1] = countEntries;
			
			return result;
	    	
	    	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
		}
	    
	    return null;
    
    }
    
    
	/**
     * Search method for an organisation member
     * @param emailAdress - email address of the user
     * @param keyWord - the search term
     * @param dateOrder - indicates if emails are shown in ascending or descending order based on date
     * @param startDate - filters to only show users emails from the given date onwards
     * @param endDate - filters to only show users emails before the given date
     * @param senderEmail - for when emails are send based on the individual
     * @param receiverEmail - for when emails are send based on the individual
     * @param length - limits amount of records processed
     * @param start - starting position for which emails to get
     * @param hasAttachment - boolean to check if email has an attachment
     * @return An object array containing the resulting email objects
     */
    public Object[] searchByKeywordAssociationMember(String emailAdress, String keyWord, String dateOrder, String startDate, String endDate,
    		String senderEmail, String receiverEmail, int length, int start, boolean hasAttachment) {
    	
        
    	String userEmail = emailAdress;
    	int userId = getUserId(userEmail);

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);
	    	
	
			//reduce all spaces to one space
			//remove spaces in front of the keyword 
	    	keyWord = keyWord.replaceAll("\\s+", " ");
	    	if (keyWord.startsWith(" ")) {
	    		keyWord = keyWord.substring(1);
	    	}
			String[] queryWords = keyWord.split("[ .,!;:-]+");
			String joinKeyWords = String.join("&", queryWords);
			
			String senderEmailQuery = "";
			String receiverEmailQuery = "";
			String keyWordQuery = "";
			String startDateQuery = "";
			String endDateQuery = "";
			String hasAttachmentQuery = "";
			
			if (!receiverEmail.isEmpty()) {
				receiverEmailQuery  = " AND p.email = ?";
			}
			
			if (!keyWord.isEmpty()) {
				keyWordQuery = " AND to_tsvector(c.subject||' '||c.content||' '||d.content||' '||d.title||' '||d.type)"
						+ "@@ to_tsquery('english',?)";
			}
			
			if (!senderEmail.isEmpty()) {
				senderEmailQuery = " AND c.senderEmail = ?";
			}
			
			if (!startDate.isEmpty()) {
				startDateQuery = " AND c.send_at >= ?";
			}
			
			if (!endDate.isEmpty()) {
				endDateQuery = " AND c.send_at <= ?";
			}
			
			if(hasAttachment) {
				hasAttachmentQuery = " AND d.document_id != 0";
			}
			
			
			//search for keyword in the email's title, content or attachments
			String query = " SELECT c.id, c.event_id, c.person_id,"
            + " c.organisation_id, c.senderemail, c.subject,"
            + " c.content, c.priority, c.send_at , c.sender, o.name, "
            + " jsonb_agg(DISTINCT jsonb_build_object('id', d.document_id, 'title', d.title, 'type', d.type) ) as attachments"
	    	+ " FROM communication c, organisation o, organisationperson op, document_email de, documents d, person p"
	    	+ " WHERE ("
	    	+ "    (c.person_id = " + userId + " AND c.organisation_id = 0)"
	    	+ " OR (c.person_id = 0 "
					+ " AND op.person_id = " + userId + " "
					+ " AND op.organisation_id = o.id)"
	    	+ " OR (c.sender = o.name)"
	    	+ " OR (c.senderemail ='" + emailAdress + "') "
	    	+ " )"
            + " AND o.id = c.organisation_id"
            + " AND p.id = c.person_id"

            + " AND de.communication_id = c.id"
            + " AND de.document_id = d.document_id"

	    	+ keyWordQuery
			+ senderEmailQuery
			+ receiverEmailQuery
			+ startDateQuery
			+ endDateQuery
			+ hasAttachmentQuery
            + " GROUP BY c.id, c.event_id, c.person_id, c.organisation_id, "
            + " c.senderemail, c.subject, c.content, c.priority, "
            + " c.send_at, c.sender, o.name "
	    	+ " ORDER BY c.send_at " + dateOrder
	    	+ " LIMIT " +  length + " OFFSET " + start + ";";
			
			
			//count the amount of emails that will be returned
			String count = "SELECT COUNT(DISTINCT c.id),"
			+ " jsonb_agg(jsonb_build_array(d.document_id) ) "
			+ " FROM communication c, organisation o, organisationperson op, documents d, document_email de, person p"
	    	+ " WHERE ("
	    	+ "    (c.person_id = " + userId + " AND c.organisation_id = 0)"
	    	+ " OR (c.person_id = 0 "
					+ " AND op.person_id = " + userId + " "
					+ " AND op.organisation_id = o.id)"
	    	+ " OR (c.sender = o.name)"
	    	+ " OR (c.senderemail ='" + emailAdress + "') "
	    	+ " )"
            + " AND o.id = c.organisation_id"
            + " AND p.id = c.person_id"

            + " AND de.communication_id = c.id"
            + " AND de.document_id = d.document_id"
	    	+ keyWordQuery
			+ senderEmailQuery
			+ receiverEmailQuery
			+ startDateQuery
			+ endDateQuery
			+ hasAttachmentQuery;
			
	    	PreparedStatement statement = connection.prepareStatement(query);
	    	
	    	prepareStatement(statement, joinKeyWords, "", senderEmail, receiverEmail, startDate, endDate);
	    	
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	EmailDao emailDao = new EmailDao();
	    	
			//display after query: sender@email, sender, title, preview content, date (show if attach? (later))
	    	while(resultSet.next()) {
				Email newEmail = addEmail(resultSet);
				emailDao.addEmail(newEmail.getId(), newEmail);
			}
			statement.close();

			statement = connection.prepareStatement(count);
			
			prepareStatement(statement, joinKeyWords, "", senderEmail, receiverEmail, startDate, endDate);
	    	
	    	resultSet = statement.executeQuery();
			
	    	int countEntries = 0;
	    	
	    	while(resultSet.next()) {
	    		countEntries = resultSet.getInt(1);
	    	}
	    	
	    	statement.close();
			connection.close();
			
			Object[] result = new Object[2];
			result[0] = emailDao;
			result[1] = countEntries;
			
			return result;
	    	
	    	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
		}
	    
	    return null;
    
    }
    
    
	/**
	 * Method retrives an email object based on its id.
	 * @param id - emailid
	 * @return an email object
	 */
    public Email getEmailById(int id) {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);
	    	
	    	String queryIdUser = "SELECT"
	    		    + " c.senderemail, c.subject, c.content,"
	    		    + " c.send_at, c.sender, c.organisation_id, c.person_id,"
	    		    + " jsonb_agg(json_build_object('title', d.title, 'type', d.type) ) as attachments"
	    			+ " FROM communication c, document_email de, documents d"
	    			+ " WHERE c.id = '" + id + "'"
	    			//+ " AND o.id = op.organisation_id"
	    			//+ " AND op.person_id = c.person_id"
                    + " AND de.communication_id = c.id"
                    + " AND de.document_id = d.document_id"
                    + " GROUP BY"
                    + " c.senderemail, c.subject, c.content,"
                    + " c.send_at, c.sender, c.organisation_id, c.person_id";

	    	Statement statement = connection.createStatement();
	    	ResultSet resultSet = statement.executeQuery(queryIdUser);
	    	
	   
	    	Email email = new Email();
	    	
	    	
	    	while(resultSet.next()) {
		
		    String receiver = "";

				if (resultSet.getInt("organisation_id") == 0) {
				   receiver = getPersonReceiver(resultSet.getInt("person_id"));
				} else {
				   receiver = getOrganisationReceiver(resultSet.getInt("organisation_id"));
				}
			
			Gson gson = new Gson();
			   
		    String attachments = resultSet.getString("attachments");

		    Attachment[] attachmentsArray = gson.fromJson(attachments, Attachment[].class);
		    List<Attachment> attachmentList = new ArrayList<>();
		   
		   
		    for(Attachment attachment: attachmentsArray) {
			  	 attachmentList.add(attachment);
		    }
	    		
	    		 email = new Email(resultSet.getString("senderemail"), resultSet.getString("subject"), 
	    				 resultSet.getString("content"), resultSet.getString("send_at"), 
						resultSet.getString("sender"), attachmentList, receiver);
						
	    	}
			
			statement.close();
			statement = connection.createStatement();
			
			return email;
  	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return null;
		}	    
    	
    }

	/**
	 * Method that uses a person id to find their name
	 * @param id - the receiver's person id
	 * @return the receiver's name
	 */
	public String getPersonReceiver(int id){
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);
	    	
	    	String queryId = "SELECT p.name"
	    			+ " FROM person p"
	    			+ " WHERE p.id = '" + id + "'";

	    	Statement statement = connection.createStatement();
	    	ResultSet resultSet = statement.executeQuery(queryId);

			String receiver = "";

	    	while(resultSet.next()) {
				receiver = resultSet.getString("name");
		    }
			
			statement.close();
			statement = connection.createStatement();
			
			return receiver;

  	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return null;
		}	    
	}
	
	/**
	 * Method that receives an organisation id to find its name
	 * @param id - the id of the receiving organisation
	 * @return the name of the receiving organisation
	 */
	public String getOrganisationReceiver(int id) {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);
	    	
	    	String queryId = "SELECT o.name"
	    			+ " FROM organisation o"
	    			+ " WHERE o.id = '" + id + "'";

	    	Statement statement = connection.createStatement();
	    	ResultSet resultSet = statement.executeQuery(queryId);

			String receiver = "";

	    	while(resultSet.next()) {
				receiver = resultSet.getString("name");
		    }
			
			statement.close();
			statement = connection.createStatement();
			
			return receiver;

  	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return null;
		}	    
	}
	
	/**
	 * Adds a new entry in the database with information to who and when an email was read and which.
	 * @param emailId - an email's id
	 * @param userEmail - the current user's email address
	 */
	public void markReadEmail(int emailId, String userEmail) {
		int personId = getUserId(userEmail);
		
		
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);

	    	//values inserted: comunication_id, person_id, date_time
	    	String readQuery = "INSERT INTO read_email "
			+ " VALUES (?, ?, LOCALTIMESTAMP);";


	    	PreparedStatement statement = connection.prepareStatement(readQuery);
			statement.setInt(1, emailId);
			statement.setInt(2, personId);
			statement.executeQuery();
			
			statement.close();
			connection.close();
  	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
		}	    
	}
	
	/**
	 * Adds a new entry in the database with information to who and when a document was read and which.
	 * @param docId - a document's id
	 * @param userEmail - the current user's email address
	 */
	public void markReadDoc(int docId, String userEmail){
		int personId = getUserId(userEmail);
		
		
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);

	    	//values inserted in: document_id, person_id, date_time
	    	String readQuery = "INSERT INTO read_docs "
			+ " VALUES (?, ?, LOCALTIMESTAMP);";


	    	PreparedStatement statement = connection.prepareStatement(readQuery);
			statement.setInt(1, docId);
			statement.setInt(2, personId);
			statement.executeQuery();
			
			statement.close();
			connection.close();
  	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
		}	    
	}
	

	/**
	 * Get who read the doc by the id of the doc
	 * @param document_id - id of the document
	 * @return an array of the people read the document
	 */
	public PersonReadDoc[] getReadDoc(int document_id) {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);

	   
	    	String readQuery = "SELECT jsonb_agg(json_build_object('personEmail', p.email, 'date', rd.date_time) ) as person_read "
			+ "FROM read_docs rd, person p "
			+ "WHERE p.id = rd.person_id "
			+ "AND rd.document_id = " + document_id;

 			Statement statement = connection.createStatement();
	    	ResultSet resultSet = statement.executeQuery(readQuery);

			Gson gson = new Gson();
			String personReads = "";
			while(resultSet.next()) {
				personReads = resultSet.getString("person_read");
			}

		    PersonReadDoc[] personReadArray = gson.fromJson(personReads, PersonReadDoc[].class);
			
			statement.close();
			connection.close();
			
			return personReadArray;
  	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return null;
		}
	}
	
	/**
	 * Get the users who read the email by the id of the email
	 * @param email_id - Id of the email
	 * @return an array of the users who read the email
	 */
	public PersonReadEmail[] getReadEmail(int email_id) {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);

	    	
	    	String readQuery = "SELECT jsonb_agg(json_build_object('personEmail', p.email, 'date', re.date_time) ) as person_read "
			+ "FROM read_email re, person p "
			+ "WHERE p.id = re.person_id "
			+ "AND re.communication_id = " + email_id;

 			Statement statement = connection.createStatement();
	    	ResultSet resultSet = statement.executeQuery(readQuery);
	    	
	    	
			Gson gson = new Gson();
			
			String personReads = "";
			while(resultSet.next()) {
				personReads = resultSet.getString("person_read");
				
				
			}

		    PersonReadEmail[] personReadArray = gson.fromJson(personReads, PersonReadEmail[].class);
			
			statement.close();
			connection.close();

			return personReadArray;
			
			
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return null;
		}
	}

	
	/**
	 * Method to find UserId by using their credentials (email address)
	 * @param userEmail - current users email address
	 * @return userId - personId associated to that email address
	 */
    private int getUserId(String userEmail) {

		int userId = 0;
	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);
	    	
			//search for id in the database based on email of user
	    	String queryIdUser = "SELECT p.id\n"
	    			+ "FROM person p\n"
	    			+ "WHERE p.email = ?"; //received from oauth

	    	PreparedStatement statement = connection.prepareStatement(queryIdUser);
	    	statement.setString(1, userEmail);
	    	ResultSet resultSet = statement.executeQuery();

	    	while(resultSet.next()) {
	    		userId = resultSet.getInt(1);
	    	}
	    	statement.close();
			connection.close();
			return userId;
  	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return 0;
		}	    
    }
    
    /**
     * Get a list of all organisations from the database
     * @return allOrganisations
     */
    public ArrayList<String> allOrganisations() {
	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);	
	    	
	    	String queryIdUser = "SELECT o.name"
	    			+ " FROM organisation o;";

	    	Statement statement = connection.createStatement();
	    	ResultSet resultSet = statement.executeQuery(queryIdUser);
	    	
	    	ArrayList<String> organisations = new ArrayList<String>();
	    	
	    	while(resultSet.next()) {

	    		organisations.add(resultSet.getString(1));
	    		
				}
	    	
				statement.close();
				connection.close();
				
				return organisations;
  	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return null;
		}	    
    	
    	
    }

	/**
	 * Gets a new email to return
	 * @param resultSetEmailsUser
	 * @return email object
	 */
	private Email addEmail(ResultSet resultSetEmailsUser) {
	    try {
		    Gson gson = new Gson();
			   
		    String attachments = resultSetEmailsUser.getString("attachments");

		    Attachment[] attachmentsArray = gson.fromJson(attachments, Attachment[].class);
		    List<Attachment> attachmentList = new ArrayList<>();
		   
		   
		    for(Attachment attachment: attachmentsArray) {
			  attachmentList.add(attachment);
		}
		   
		    
		    
		    Email newEmail = new Email(resultSetEmailsUser.getInt("id"), resultSetEmailsUser.getInt("event_id"), resultSetEmailsUser.getInt("person_id"), 
					    resultSetEmailsUser.getInt("organisation_id"), resultSetEmailsUser.getString("senderemail"), resultSetEmailsUser.getString("subject"), 
						resultSetEmailsUser.getString("content"), resultSetEmailsUser.getInt("priority"), resultSetEmailsUser.getString("send_at"), 
						attachmentList, resultSetEmailsUser.getString("sender"), resultSetEmailsUser.getString("name"));	               
	       
		    
		    return newEmail;
		    
		} catch (SQLException e) {
			System.err.println("Error connecting: " + e);
			return null;
		}  
		  
	}

	/**
	 * Method that sets the values of the PreparedStatement that is given
	 * @param statement - The PreparedStatement to be filled
	 * @param joinKeyWords - input from query 
	 * @param organisation - input from query 
	 * @param senderEmail - input from query 
	 * @param receiverEmail - input from query 
	 * @param startDate - input from query 
	 * @param endDate - input from query 
	 */
	private void prepareStatement(PreparedStatement statement, String joinKeyWords, String organisation,
			String senderEmail, String receiverEmail, String startDate, String endDate) {
		
    	int prepStatmentCount = 1;
    	try{
			if (!joinKeyWords.isEmpty()) {
					statement.setString(prepStatmentCount, joinKeyWords);
					prepStatmentCount += 1;
				}
		    	
				if (!organisation.isEmpty()) {
					statement.setString(prepStatmentCount, organisation);
					prepStatmentCount += 1;
				}
				
				if (!senderEmail.isEmpty()) {
					statement.setString(prepStatmentCount, senderEmail);
					prepStatmentCount += 1;
				}
				
				if (!receiverEmail.isEmpty()) {
					statement.setString(prepStatmentCount, receiverEmail);
					prepStatmentCount += 1;
				}
				
				if (!startDate.isEmpty()) {
					Timestamp startTP = Timestamp.valueOf(startDate);
					statement.setTimestamp(prepStatmentCount, startTP);
					prepStatmentCount += 1;
				}
				
				if (!endDate.isEmpty()) {
					Timestamp endTP = Timestamp.valueOf(endDate);
					statement.setTimestamp(prepStatmentCount, endTP);
					prepStatmentCount += 1;
				}
			} catch (SQLException e) {
				System.out.println("SQL error occured");
			}		
	}
	
	/**
	 * Get organisation of user by his email
	 * @param email - email address
	 * @return string organisation
	 */
	public String getUserOrganisation(String email) {
		
		int UserId = getUserId(email);
		
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);	
	    	
	    	String queryGetOrganisaiton = "SELECT o.name"
	    			+ " FROM organisationperson op, organisation o"
	    			+ " WHERE op.organisation_id = o.id"
	    			+ " AND op.person_id = ?";

	    	PreparedStatement statement = connection.prepareStatement(queryGetOrganisaiton);
	    	statement.setInt(1, UserId);
	    	ResultSet resultSet = statement.executeQuery();
	    	
	    	String organisation = "";
	    	while(resultSet.next()) {
				organisation = resultSet.getString(1);
			}
		
			statement.close();
			connection.close();
			
			return organisation;
  	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return null;
		}
		
		
	}
	
	/**
	 * Checks if an organisation member is allowed to access the email
	 * @param emailAddress - email address of user
	 * @param emailId - Id of the email the user wants to access
	 * @return true if user is allowed to access the email
	 */
	public boolean HasPermissionForEmail(String emailAddress, int emailId) {
		
		String userEmail = emailAddress;
    	int userId = getUserId(userEmail);

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);
	    	
			String query = " SELECT c.id "
			+ "FROM communication c, organisationperson op, person p, organisation o"
	    	+ " WHERE ("
	    	+ "    (c.person_id = " + userId + " AND c.organisation_id = 0)"
	    	+ " OR (c.person_id = 0 "
					+ " AND op.person_id = " + userId + " "
					+ " AND op.organisation_id = o.id)"
	    	+ " OR (c.sender = o.name)"
	    	+ " OR (c.senderemail ='" + emailAddress + "') "
	    	+ " )"
            + " AND o.id = c.organisation_id"
            + " AND p.id = c.person_id;";
			
			Statement statement = connection.createStatement();
	    	ResultSet resultSet = statement.executeQuery(query);
	    	
	    	boolean permission = false;
	    	
	    	while(resultSet.next()) {
	    		if (resultSet.getInt(1) == emailId) {
	    			permission = true;
	    		}
			}
	    	
	    	
			statement.close();
			connection.close();
			
			return permission;
	    	
	    	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
		}
	    return false;
	    
	}
	
	/**
	 * Check if the user (by his emailaddress) has acccess to a document
	 * @param emailAddress - of user
	 * @param document - document title & type
	 * @return boolean true if user is allowed to access the document
	 */
	public boolean HasPermissionForDocument(String emailAddress, String document) {
		
		String userEmail = emailAddress;
    	int userId = getUserId(userEmail);

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);
	    	
			String query = " SELECT d.title, d.type "
			+ "FROM communication c, organisationperson op, person p, organisation o,"
			+ " document_email de, documents d"
	    	+ " WHERE ("
	    	+ "    (c.person_id = " + userId + " AND c.organisation_id = 0)"
	    	+ " OR (c.person_id = 0 "
					+ " AND op.person_id = " + userId + " "
					+ " AND op.organisation_id = o.id)"
	    	+ " OR (c.sender = o.name)"
	    	+ " OR (c.senderemail ='" + emailAddress + "') "
	    	+ " )"
            + " AND o.id = c.organisation_id"
            + " AND p.id = c.person_id"
            + " AND de.document_id = d.document_id"
            + " AND de.communication_id = c.id";
			
			Statement statement = connection.createStatement();
	    	ResultSet resultSet = statement.executeQuery(query);
	    	
	    	boolean permission = false;
	    	
	    	
	    	while(resultSet.next()) {
	    		if ((resultSet.getString(1)+"."+resultSet.getString(2)).equals(document)) {
	    			permission = true;
	    		}
			}
	    	
			statement.close();
			connection.close();
			
			return permission;
	    	
	    	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
		}
	    return false;
	    
	}
	
	/**
	 * Get the id of the document
	 * @param document - name of the document
	 * @return int documentID
	 */
	public int getDocumentID(String document) {
		
		int split = document.lastIndexOf(".");
		String title = document.substring(0, split);
		String type = document.substring(split + 1);
		
	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);
	    	
			String query = " SELECT d.document_id "
			+ "FROM documents d"
	    	+ " WHERE d.title = ?"
			+ " AND d.type = ?;";
			
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			
			preparedStatement.setString(1, title);
			preparedStatement.setString(2, type);
			
	    	ResultSet resultSet = preparedStatement.executeQuery();
	    	
	    	int doc_id = -1;
	    	
	    	while(resultSet.next()) {
	    		doc_id = resultSet.getInt(1);
			}
	    	
	    	preparedStatement.close();
			connection.close();
			
			return doc_id;
	    	
	    	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
		}
	    return -1;
	    
	}
	
	/**
	 * Method that returns user information in an array. 
	 * Information in array is: user name, user email and user's organisation
	 * @return String array with user info
	 */
	public ArrayList<String> allPeople() {
	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);	
	    	
	    	String queryIdUser = "SELECT p.name, p.email, o.name"
	    			+ " FROM person p, organisationperson op, organisation o"
	    			+ " WHERE o.id = op.organisation_id"
	    			+ " AND p.id = person_id";

	    	Statement statement = connection.createStatement();
	    	ResultSet resultSet = statement.executeQuery(queryIdUser);
	    	
	    	ArrayList<String> people = new ArrayList<String>();
	    	
	    	while(resultSet.next()) {

	    		people.add(resultSet.getString(1)+" ("+resultSet.getString(2)+") " + "- " + resultSet.getString(3));
	    		
				}
				statement.close();
				connection.close();
				
				return people;
  	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return null;
		}
	}
	
	/**
	 * Method that returns information from a document 
	 * Information in array: document title and document type
	 * @return String array with document info
	 */
	public ArrayList<String> allDocuments() {
	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);	
	    	
	    	String queryIdUser = "SELECT d.title, d.type"
	    			+ " FROM documents d;";

	    	Statement statement = connection.createStatement();
	    	ResultSet resultSet = statement.executeQuery(queryIdUser);
	    	
	    	ArrayList<String> documents = new ArrayList<String>();
	    	
	    	while(resultSet.next()) {
	    		if (!(resultSet.getString(1).isEmpty())) {
	    			documents.add(resultSet.getString(1)+"."+resultSet.getString(2));
	    		}
	    		
			}
				statement.close();
				connection.close();
				
				return documents;
  	
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return null;
		}
	}
	
	/**
	 * Method that adds new emails and document to the database with all the necessary information. 
	 * Only kick-in members can invoke this method in the front-end.
	 * @param receiver - receiving person or organisation name
	 * @param subject - subject of email
	 * @param sender - user who sends it
	 * @param senderEmail - the sending user's email address
	 * @param content - content of email
	 * @param date - date at which it's send
	 * @param docs - documents attached to the email if wanted
	 * @throws Exception This exception will be handled in the ResourceUpload and a proper error will 
	 * be displayed to the user.
	 */
	public void addNewCommunication(String receiver, String subject, String sender, 
		String senderEmail, String content, String date,
		List<String> docs) throws Exception {

		if (!sanitizeNewCommuication(receiver, subject, sender, senderEmail, content)) {
			throw new Exception();
		}
		
		int communicationId = getNewCommunicationId();
		
		Class.forName(JDBC_DRIVER);
		Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);	
	
		String addCommunication = "INSERT INTO communication(id, person_id, organisation_id, senderemail,"
			+ "subject, content, send_at, sender, event_id)"
			+ "VALUES (" + communicationId + ",?,?,?,?,?,?,?, 9)";
	
		PreparedStatement statement = connection.prepareStatement(addCommunication);
		
		if(receiver.contains("@")) {
			int personId = getUserId(receiver.split("\\(")[1].split("\\)")[0]);
			statement.setInt(1, personId);
			statement.setInt(2, 0);
		} else {
			int organisationId = getOrganisationId(receiver);
			statement.setInt(1, 0);
			statement.setInt(2, organisationId);
		}
	
		Timestamp dateTime = Timestamp.valueOf(date);
		
		statement.setString(3, senderEmail);
		statement.setString(4, subject);
		statement.setString(5, content);
		statement.setTimestamp(6, dateTime);
		statement.setString(7, sender);
		statement.execute();
		
		statement.close();
		connection.close();
	}
	
	/**
	 * Method that creates a new communication id for a newly created email
	 * @return unique int communication id
	 */
	private int getNewCommunicationId(){
		int newID = 0;
	
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	
		try {
			Connection connection =
			DriverManager.getConnection(DB_URL, USER, PASS);	
			
			String idQuery = "SELECT MAX(c.id) FROM communication c";
				
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(idQuery);
			
			while(resultSet.next()) {
				newID = resultSet.getInt(1);
			}
	
			statement.close();
			connection.close();
			
			return newID + 1;
			
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return 0;
		}
	}
	 
	/**
	 * Method that sanitizes the new communication from malicious code or possible harmful characters.
	 * @param receiver - receiving person or organisation name
	 * @param subject - subject of email
	 * @param sender - user who sends it
	 * @param senderEmail - the sending user's email address
	 * @param content - content of email
	 */
	private boolean sanitizeNewCommuication(String receiver, String subject, String sender, String senderEmail, String content){
		boolean goodInput = true;
		
		String userInput = receiver+subject+sender+senderEmail+content;
		String lowerInput = userInput.toLowerCase();

		if (lowerInput.contains("</script")) {  
			goodInput = false;
		}

		goodInput = Pattern.matches("[a-zA-Z0-9!.:;?)@#(\\s-]+", userInput);
		return goodInput;
	}
	
	/**
	 * Returns an ID of the organisation
	 * @param orgName - name of the organisation
	 * @return ID of the needed organisation
	 */
	private int getOrganisationId(String orgName) {
		int orgID = 0;
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	
		try {
			Connection connection =
			DriverManager.getConnection(DB_URL, USER, PASS);	
			
			String idQuery = "SELECT o.id FROM organisation o WHERE o.name = ?";
			
			PreparedStatement statement = connection.prepareStatement(idQuery);
			
			statement.setString(1, orgName);
			ResultSet resultSet = statement.executeQuery();
			
			
			while(resultSet.next()) {
				orgID = resultSet.getInt(1);
			}
			
			statement.close();
			connection.close();
			
			return orgID;
			
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
		return 0;
		}
	}
	
	/**
	 * Returns new ID of the record by incrementing the highest ID in the table by 1
	 * @return new ID of the organisation to be added 
	 */
	private int getNewOrganisationId() {
		int newID = 0;

		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
		
		try {
			Connection connection =
			DriverManager.getConnection(DB_URL, USER, PASS);
			
			String idQuery = "SELECT MAX(o.id) FROM organisation o";
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(idQuery);	
			
			while(resultSet.next()) {
				newID = resultSet.getInt(1);
			}
			
			statement.close();
			connection.close();
			
			return (newID + 1);
			
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return 0;
		}
	}
	
	/**
	 * Adds a new organisation
	 * @param organisation - name of the organisation to be added
	 * @throws Exception This exception will be handled in the ResourceUpload and a proper error will 
	 * be displayed to the user.
	 */
	public void addNewOrganisation(String organisation) throws Exception {
	
		int organisationId = getNewOrganisationId();
	
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
		
		Connection connection =
		DriverManager.getConnection(DB_URL, USER, PASS);
		
		String addOrganisation = "INSERT INTO organisation(id, name, parent_id)"
				+ "VALUES (" + organisationId + ",?,0)";
		
		PreparedStatement statement = connection.prepareStatement(addOrganisation);
		statement.setString(1, organisation);
		statement.execute();
		
		statement.close();
		connection.close();
	
	}
	
	/**
	 * Checks if the organisation already exists
	 * @param orgName - name of the organisation
	 * @return true or false
	 */
	public boolean checkExistOrganisation(String orgName) {
		return allOrganisations().contains(orgName);
	}
	
	/**
	 * Checks if the  user exists 
	 * @param perEmail - email of the person
	 * @return true if person exists, otherwise false
	 */
	public boolean checkExistPerson(String perEmail) {
		List<String> people = allPeople();
		
		for (String person: people) {
			if (person.contains(perEmail)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	/**
	 * Returns new ID of the record by incrementing the highest ID in the table by 1
	 * @return new ID of the person
	 */
	private int getNewPersonId() {
		int newID = 0;
		
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
		
		try {
			Connection connection =
			DriverManager.getConnection(DB_URL, USER, PASS);
			
			String idQuery = "SELECT MAX(p.id) FROM person p";
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(idQuery);
			
			while(resultSet.next()) {
				newID = resultSet.getInt(1);
			}
			
			statement.close();
			connection.close();
			
			return (newID + 1);
			
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return 0;
		}
	}
	
	/**
	 * Returns new ID of the record by incrementing the highest ID in the table by 1
	 * @return new ID of the record
	 */
	private int getNewOrgPersonId() {
		int newID = 0;
		
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
		
		try {
			Connection connection =
			DriverManager.getConnection(DB_URL, USER, PASS);
			
			String idQuery = "SELECT MAX(op.id) FROM organisationperson op";
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(idQuery);
			
			while(resultSet.next()) {
				newID = resultSet.getInt(1);
			}
			
			statement.close();
			connection.close();
			
			return (newID + 1);
			
		} catch (SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
			return 0;
		}
	}
	
	/**
	 * Adds a new user to the database 
	 * @param organisation - Name of the organisation the added person belongs to
	 * @param name - Name of the person
	 * @param email - Email of the person
	 * @throws Exception This exception will be handled in the ResourceUpload and a proper error will 
	 * be displayed to the user.
	 */
	public void addNewPerson(String organisation, String name, String email) throws Exception {
	
		int personId = getNewPersonId();
		int orgPerson = getNewOrgPersonId();
		
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
		
		Connection connection =
		DriverManager.getConnection(DB_URL, USER, PASS);
		
		Database db = new Database();
		int orgId = db.getOrganisationId(organisation);
		
		String addPerson = "INSERT INTO person(id, email, name)"
				+ "VALUES (" + personId + ",?,?)";
		
		PreparedStatement statementPerson = connection.prepareStatement(addPerson);
		statementPerson.setString(1, email);
		statementPerson.setString(2, name);
		statementPerson.execute();
		
		statementPerson.close();
		
		String addOrgPerson = "INSERT INTO organisationperson(id, event_id, person_id, organisation_id)"
				+ "VALUES ("+orgPerson+","+"2,"+personId+","+orgId+")";
		
		
		Statement statementOrgPerson = connection.createStatement();
		statementOrgPerson.execute(addOrgPerson);
		
		statementOrgPerson.close();
		connection.close();
	}
	
}
