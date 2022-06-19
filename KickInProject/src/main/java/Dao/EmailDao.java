package Dao;

import java.util.HashMap;
import java.util.Map;

import Model.Email;

public class EmailDao {

    //Hashmap that contains email objects with their Id as key
    private Map<Integer, Email> emails = new HashMap<Integer, Email>();
    
    /**
     * Clears all email objects from the map
     */
    public void clearEmails() {
        emails.clear();
    }

// ------------ SETTERS & GETTERS --------------

    /**
	 * Returns a map that contains email objects
	 * @return hashmap with email objects
	 */
	public Map<Integer, Email> getData() {
        return emails;
    }

	/**
	 * Adds email objects to a map with its email Id as a key
	 * @param id - email Id
	 * @param email - email object 
	 */
    public void addEmail(int id, Email email) {
        emails.put(id, email);
    }
    
    /**
     * Returns email objects by its Id, which is a key
     * @param id - emailId
     * @return email object
     */
    public Email getEmail(int id) {
    	return emails.get(id);
    }

    


}