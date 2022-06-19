package Dao;

import java.util.HashMap;
import java.util.Map;

import Model.User;

public enum UserDao {

	instance;

	//Map that stores user objects with access token as the key
    private Map<String, User> users = new HashMap<String, User>();
    
	//Map that stores access tokens with the users email address as the key
    private Map<String, String> inverseUsers = new HashMap<String, String>();
    
	/**
	* method to add new users
	* @param accessToken token of this user
	* @param user is the user which has an email, name and organisation
	*/
    public void addUser(String accessToken, User user) {
    	users.put(accessToken, user);
    }
    
	/**
	* get a user by his acces token
	* @param accessToken - token of the user
	* @return an object User from a map users
	*/
	public User getUser(String accessToken) {
    	return users.get(accessToken);
    }

    /**
     * remove user by his access token
     * @param accessToken token of the user
     */
    public void removeUser(String accessToken) {
    	users.remove(accessToken);
    }
	
    /**
     * Checks if user has a session key for the web application
     * @param accessToken - user session key
     * @return true if user has a session key
     */
	public boolean checkSession(String accessToken) {
		return users.containsKey(accessToken);
	}
    
	/**
	 * Get all users
	 * @return Map users
	 */
    public Map<String, User> getUsersMap() {
    	return users;
    }
    
	/**
	 * add a new user in the map with email-token
	 * @param email - email address of the user
	 * @param token - access token of the user
	 */
	public void addInverseUser(String email, String token) {
		inverseUsers.put(email, token);
	}
	
	/**
	 * get the accesstoken of a user with the email that is the param
	 * @param email - email address of the user
	 * @return String which is the accesstoken
	 */
	public String getInverseUser(String email) {
		return inverseUsers.get(email);
	}
	
	/**
	 * check if a user exists with this email 
	 * @param email - an email address
	 * @return boolean that is true if map contains email address key
	 */
	public boolean checkForExistingEmail(String email) {
		return inverseUsers.containsKey(email);
	}
	/**
	 * remove user by his email
	 * @param email - an email address
	 */
	public void inverseUsers(String email) {
		inverseUsers.remove(email);
	}
	
	/**
	 * get all userEmails with their belonging accessToken
	 * @return Map with key-userEmail and value-accessToken
	 */
	public Map<String, String> getInverseUserMap() {
		return inverseUsers;
	}


}
