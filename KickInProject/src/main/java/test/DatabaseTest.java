package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

import Model.Attachment;
import Model.Email;
import Model.PersonReadDoc;
import Model.PersonReadEmail;
import Resource.Database;
/**
*All tests are done based on the sample data in our database server.
*/
class DatabaseTest {
    static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String host = "bronto.ewi.utwente.nl";
	static final String dbName = "dab_di20212b_100";
	static final String DB_URL = "jdbc:postgresql://" + host + ":5432/" + 
									dbName +"?currentSchema=kickindatabase";
	
	static final String USER = "dab_di20212b_100";
	static final String PASS = "Txc5x85GyM/DPALd";
	static final int TOTAL_EMAIL_COUNT = 24;
	static final String emailAssociationMember = "puzzleinfo@puzzlemania.nl";
	
	private Database database;
	
	/*
	 * Initialising the database to be tested.
	 */
	@BeforeEach
	void setUp() {	
		database = new Database();
	}

	/*
	 * Test if there are 24 emails in total (pre-loaded in the database) for the Kick-in committee member
	 */
	@Test
	void countEmailsKickInMember(){
		int testTotalCount = database.countEmailsKickInMember();
		assertEquals(TOTAL_EMAIL_COUNT, testTotalCount);

	}

	/*
	 * Test if there are 21 emails in total (pre-loaded in the database) for an association member.
	 */
	@Test
	void countEmailAssociationMember() {
		int testTotalCount = database.countEmailsAssociationMember(emailAssociationMember);
		assertEquals(21, testTotalCount);

	}
	
	/*
	 * Test if a query filtered on a key word and a specific organisation returns the desired results
	 * (Kick-in committee member)
	 */
	@Test
	void searchByKeywordKickInMemberOrganisation(){
		String keyword = "kick in";
		int pageLength = 20; //max results shown on one page
		int expectedQueryResult = 16;
		int actualQueryResult = (int) database.searchByKeywordKickInMember(keyword, "", "", "", "SecondOrganisation", "", "", 20, pageLength, false)[1];
		assertEquals(expectedQueryResult, actualQueryResult);
	}

	/*
	 * Test if a query filtered on a key word returns the desired results
	 * (association member)
	 */
	@Test
	void searchByKeywordAssociationMember(){
		String keyword = "members";
		int pageLength = 20; //max results shown on one page
		int expectedQueryResult = 2;
		int actualQueryResult = (int) database.searchByKeywordAssociationMember(
			emailAssociationMember, keyword, "", "", "", "", "", 20, pageLength, false)[1];
		assertEquals(expectedQueryResult, actualQueryResult);

	}
	
	/*
	 * Test if a query filtered on a key word and start and end date of emails returns the desired results
	 * (association member)
	 */
	@Test
	void searchByKeywordAssociationMemberDate(){
		String keyword = "association";
		int pageLength = 20; //max results shown on one page
		int expectedQueryResult = 7;
		int actualQueryResult = (int) database.searchByKeywordAssociationMember(
			emailAssociationMember, keyword, "", "2020-04-08 00:00:00", "2020-06-18 00:00:00", "", "", 20, pageLength, false)[1];
		assertEquals(expectedQueryResult, actualQueryResult);

	}
	
	/*
	 * Test if a query filtered on a key word, start and end date of emails 
	 * and sender returns the desired results
	 * (association member)
	 */
	@Test
	void searchByKeywordAssociationMemberDateSender(){
		String keyword = "association";
		int pageLength = 20; //max results shown on one page
		int expectedQueryResult = 1;
		int actualQueryResult = (int) database.searchByKeywordAssociationMember(
			emailAssociationMember, keyword, "", "2020-04-08 00:00:00", "2020-06-18 00:00:00", "secretaris@kick-in.nl", "", 20, pageLength, false)[1];
		assertEquals(expectedQueryResult, actualQueryResult);

	}
	
	/*
     * Test if a query filtered on a key word, start and end date of emails 
	 * and sender, receiver returns the desired results
	 * (association member)
	 */
	@Test
	void searchByKeywordAssociationMemberDateSenderReceiver(){
		String keyword = "association";
		int pageLength = 20; //max results shown on one page
		int expectedQueryResult = 1;
		int actualQueryResult = (int) database.searchByKeywordAssociationMember(
			emailAssociationMember, keyword, "", "2020-04-08 00:00:00", "2020-06-18 00:00:00", "info@kick-in.nl", "puzzleinfo@puzzlemania.nl"
					, 20, pageLength, false)[1];
		assertEquals(expectedQueryResult, actualQueryResult);

	}
	
	/*
	 * Test if a query filtered on the has attachment filter
	 * returns the desired results
	 * (association member)
	 */
	@Test
	void searchByKeywordAssociationMemberAttachment(){
		int pageLength = 20; //max results shown on one page
		int expectedQueryResult = 12;
		int actualQueryResult = (int) database.searchByKeywordAssociationMember(
			emailAssociationMember, "", "", "", "", "", ""
					, 20, pageLength, true)[1];
		assertEquals(expectedQueryResult, actualQueryResult);
	}
	
	/*
	 * Tests if the correct organisation belonging to the user is returned
	 */
	@Test
	void getUserOrganisationTest() {
		String expectedOrganisation = "SecondOrganisation";
		String actualOrganisation = database.getUserOrganisation(emailAssociationMember);
		assertEquals(expectedOrganisation, actualOrganisation);
	}
	
	/*
	 * Tests if the email returned by a method has the right details(sender, content, subject, sendAt)
	 */
	@Test
	void getEmailById() {
		int emailId = 168291;
		
		Email actualEmailObject = database.getEmailById(emailId);
		
		String testContentSnip = "We hope that the event bureau can work on a division";
		String actualContent = actualEmailObject.getContent();
		String actualSender = actualEmailObject.getSender();
		String actualSendAt = actualEmailObject.getSendAt();
		String actualSubject = actualEmailObject.getSubject();

		assertTrue(actualContent.contains(testContentSnip));
		assertEquals("Kick-In Commissie", actualSender);
		assertEquals("2020-06-17 17:45:04", actualSendAt);
		assertEquals("OKIC Meeting Slides & Link", actualSubject);
		
	}

	/*
	 * Tests if the method returns the correct person name by ID
	 */
	@Test
	void getPersonNameById() {
		int personId = 1;
		String personName ="Penny M. Bass";
		String testPersonName = database.getPersonReceiver(personId);
		assertEquals(personName, testPersonName);
	}
	
	/*
	 * Tests if the method returns the correct organisation by ID
	 */
	@Test
	void getOrganisationNamebyId() {
		int organisationId =2;
		String organisationName ="SecondOrganisation";
		String testOrganisationName = database.getOrganisationReceiver(organisationId);
		assertEquals(organisationName, testOrganisationName);
	}

	/*
	 * Tests two methods (not best practice but here they work intuitively).
	 * Tests if methods added and retrieved entries that indicate users who have read an email correctly
	 */
	@Test
	void checkReadEmail() {
		int emailId =164844;
		database.markReadEmail(emailId, emailAssociationMember);	
		PersonReadEmail[] testReadEmail = database.getReadEmail(emailId);
		
		if (testReadEmail == null) {
			System.out.println("nothing in there");
		}
		
		int length = testReadEmail.length; //even if there are many read records with this email, 
		            					   //we will get the one that is inserted for this test
		assertEquals(emailAssociationMember, testReadEmail[length-1].getPersonEmail());	
		removeReadTestItem(false);	
	}

	/*
	 * Tests two methods (not best practice but here they work intuitively).
	 * Tests if methods added and retrieved entries that indicate users who have read a document correctly
	 */
	@Test
	void checkReadDoc() {
		//assumption that docId does not have any records yet in read_doc
		int docId =2;
		database.markReadDoc(docId, emailAssociationMember);
		PersonReadDoc[] testReadDoc = database.getReadDoc(docId);
		
		int length = testReadDoc.length; //even if there are many reads with this document,
										 //we will get the one that is inserted for this test
		assertEquals(emailAssociationMember, testReadDoc[length-1].getPersonEmail());
		removeReadTestItem(true);	
	}

	/*
	 * Tests if the method returns all 26 organisations
	 */
	@Test
	void getAllOrganisations() {
		ArrayList<String> testAllOrganisation = database.allOrganisations();
		assertEquals(26, testAllOrganisation.size());
	}
	
	/*
	 * Tests if correct document Id is returned from a documents title
	 */
	@Test
	void getDocumentIDTest() {
		String document = "info-meeting-okic-2-may-2020.pptx";
		int docId = database.getDocumentID(document);
		assertEquals(5, docId);
	}
	
	/* 
	 * Tests if an association members is denied access to emails of another organisation 
	 */
	@Test
	void checkHasPermissionEmail() {
		String email = "cheryl@cherry.nl";
		int emailId = 164844;
		boolean permission = database.HasPermissionForEmail(email, emailId);
		assertFalse(permission);
	}

	/*
	 * Tests if an association members is denied access to documents of another organisation 
	 */
	@Test
	void checkHasPermissionDoc() {
		String email = "cheryl@cherry.nl";
		String documentTitle = "doegroepouders.docx";
		boolean permission = database.HasPermissionForDocument(email, documentTitle);
		assertFalse(permission);
	}
	
	/*
	 * Test if all people registered in the database are returned
	 */
	@Test
	void allPeopleTest() {
		List<String> people = database.allPeople();
		assertEquals(18, people.size());
	}
	
	/*
	 * Test if all documents are returned
	 */
	@Test
	void allDocumentsTest() {
		List<String> docs = database.allDocuments();
		assertEquals(23, docs.size());
	}
	
	/*
	 * Tests if the organisation exists as it should
	 */
	@Test
	void checkExistingOrganisationTest() {
		boolean exist = database.checkExistOrganisation("SecondOrganisation");
		assertTrue(exist);
	}
	
	/*
	 * Tests if the organisation does not exist as it should
	 */
	@Test
	void checkNonExistingOrganisationTest() {
		boolean exist = database.checkExistOrganisation("ThatOrganisation");
		assertFalse(exist);
	}
	
	/*
	 * Tests if the person exists as it should
	 */
	@Test
	void checkExistingPersonTest() {
		boolean exist = database.checkExistPerson("petrov.k.alexander@gmail.com");
		assertTrue(exist);
	}
	
	/*
	 * Tests if the person does not exist as it should
	 */
	@Test
	void checkNonExistingPersonTest() {
		boolean exist = database.checkExistPerson("somerandomEmail@mail.nl");
		assertFalse(exist);
	}
	
	/*
	 * Tests if a new person can be added to the database
	 */
	@Test
	void addNewPersonTest() throws Exception {
		database.addNewPerson("SecondOrganisation", "Test Guy", "testing@mail.nl");
		int numOfPeople = database.allPeople().size();
		assertEquals(19, numOfPeople);
		removeAddPersonTestItem("testing@mail.nl"); 
	}
	
	/*
	 * Tests if a new organisation can be added to the database
	 */
	@Test
	void addNewOrganisation() throws Exception {
		database.addNewOrganisation("Newest Organisation");
		int numOfOrganisations = database.allOrganisations().size();
		assertEquals(27, numOfOrganisations);
		removeAddOrganisationTestItem(27);
	}
	
	/*
	 * Tests if a new communication destined to an organisation member can be added to the database
	 */
	@Test
	void addNewCommunicationOrganisationMember() throws Exception {
		List<String> randomDocs = new ArrayList<>();
		database.addNewCommunication("Testing test("+emailAssociationMember+")", "This is a test", "Kick-in", 
				"info@kick-in.nl", "Testing", "2000-01-01 00:00:00", randomDocs);
		int numOfEmails = database.countEmailsKickInMember();
		assertEquals(25, numOfEmails);
		removeAddCommunicationTestItem(185082);
	}
	
	/*
	 * Tests if a new communication destined to an organisation member can be added to the database
	 */
	@Test
	void addNewCommunicationOrganisation() throws Exception {
		List<String> randomDocs = new ArrayList<>();
		database.addNewCommunication("SecondOrganisation", "This is a test", "Kick-in", 
				"info@kick-in.nl", "Testing", "2000-01-01 00:00:00", randomDocs);
		int numOfEmails = database.countEmailsKickInMember();
		assertEquals(25, numOfEmails);
		removeAddCommunicationTestItem(185082);
	}
	
	/*
	 * Helper function to remove the test READ entries from read_email and read_docs.
	 * @param isDoc - boolean that is true when an document entry should be removed, else email
	 */
	private void removeReadTestItem(boolean isDoc){
	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);

	    	String table = "";
	    	String id = "";
	    	
	    	if (isDoc) {
	 			table = "read_docs";
	 			id = "document_id"; 
	 		} else {
	 			table = "read_email";
	 			id = "communication_id";
	 		}
	    	
	    	String queryDelete = " DELETE FROM " + table + "\r\n"
	    			+ "WHERE " + id + " IN \r\n"
	    			+ "(SELECT " + id + " \r\n"
	    			+ " FROM " + table + "\r\n"
	    			+ " ORDER BY date_time DESC \r\n"
	    			+ " LIMIT 1\r\n"
	    			+ ")";
    		
	    	Statement statementDelete = connection.createStatement();

	    	statementDelete.executeQuery(queryDelete);
	    	statementDelete.close();
	    	connection.close();
		
	    } catch(SQLException e) {
	    	System.out.println("ERROR");
	    }
	}

	/*
	 * Helper function to remove the test item from person table
	 * @param email - the email of the test person
	 */
	private void removeAddPersonTestItem(String email){
	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);

	    	
	    	String queryDelete = " DELETE FROM person\r\n"
	    			+ "WHERE id IN \r\n"
	    			+ "(SELECT id \r\n"
	    			+ " FROM person p\r\n"
	    			+ " WHERE p.email = '" + email + "'\r\n"
	    			+ ")";
    		
	    	Statement statementDelete = connection.createStatement();

	    	statementDelete.executeQuery(queryDelete);
	    	statementDelete.close();
	    	connection.close();
		
	    } catch(SQLException e) {
	    	System.out.println("ERROR");
	    }
	}
	
	/*
	 * Helper function to remove the test item from organisation table
	 * @param id - the id of the test organisation
	 */
	private void removeAddOrganisationTestItem(int id) {
	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
	    
	    try {
	    	Connection connection =
	    			DriverManager.getConnection(DB_URL, USER, PASS);

	    	
	    	String queryDelete = " DELETE FROM organisation\r\n"
	    			+ "WHERE id IN \r\n"
	    			+ "(SELECT id \r\n"
	    			+ " FROM organisation o\r\n"
	    			+ " WHERE o.id = " + id + "\r\n"
	    			+ ")";
    		
	    	Statement statementDelete = connection.createStatement();

	    	statementDelete.executeQuery(queryDelete);
	    	statementDelete.close();
	    	connection.close();
		
	    } catch(SQLException e) {
	    	System.out.println("ERROR");
	    }
	}
	
	/*
	 * Helper function to remove the test item from communication table
	 * @param id - the id of the test communication
	 */
	private void removeAddCommunicationTestItem(int id) {
	    try {
				Class.forName(JDBC_DRIVER);
			} catch (ClassNotFoundException cnfe) {
				System.err.println("Error loading driver: " + cnfe);
			}
		    
		    try {
		    	Connection connection =
		    			DriverManager.getConnection(DB_URL, USER, PASS);

		    	
		    	String queryDelete = " DELETE FROM communication\r\n"
		    			+ "WHERE id IN \r\n"
		    			+ "(SELECT id \r\n"
		    			+ " FROM communication c\r\n"
		    			+ " WHERE c.id = " + id + "\r\n"
		    			+ ")";
	    		
		    	Statement statementDelete = connection.createStatement();

		    	statementDelete.executeQuery(queryDelete);
		    	statementDelete.close();
		    	connection.close();
			
		    } catch(SQLException e) {
		    	System.out.println("ERROR");
		    }
	}
	
	

    public static void main(String[] args) {

	}

}


