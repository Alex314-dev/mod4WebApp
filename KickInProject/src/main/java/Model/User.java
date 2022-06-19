package Model;

import java.time.LocalDateTime;

public class User {
	
	private LocalDateTime  sessionExpiery;
	private String userEmail;
	private String name;
	private String organisation;
	//stored for the log-out functionality
	private String id_token;
	
	/**
	 * constructor to give email and name to a user
	 * @param userEmail
	 * @param name
	 */
	User (String userEmail, String name) {
		userEmail = this.userEmail;
		name = this.name;
	}


// ------------ SETTERS & GETTERS --------------
	/**
	 * get email of user
	 * @return userEmail - email of the user
	 */
	public String getEmail() {
		return userEmail;
	}
	/**
	 * Sets the email of the user 
	 * @param userEmail - email of the user
	 */
	public void setEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	/**
	 * get name of the user
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * set name of the user
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Returns the organisation the user belongs to
	 * @return organisation 
	 */
	public String getOrganisation() {
		return organisation;
	}
	/**
	 * set organisation the user belongs to
	 * @param organisation
	 */
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}


	public String getId_token() {
		return id_token;
	}


	public void setId_token(String id_token) {
		this.id_token = id_token;
	}


	public LocalDateTime getSessionExpiery() {
		return sessionExpiery;
	}


	public void setSessionExpiery(LocalDateTime sessionExpiery) {
		this.sessionExpiery = sessionExpiery;
	}
	
}
