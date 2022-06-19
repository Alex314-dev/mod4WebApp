package Model;

public class PersonReadEmail {
	private String personEmail;
	private String date;
	
	/**
	 * constructor which adds email and date to PersonReadEmail object
	 * @param personEmail
	 * @param date
	 */
	public PersonReadEmail(String personEmail, String date) {
		this.personEmail = personEmail;
		this.date = date;
	}
	

// --------------- GETTERS --------------
	
	/**
     * get email of the person who read the email
     * @return personEmail
     */
	public String getPersonEmail() {
		return this.personEmail;
	}
	/**
	 * get the date of when this person read the email
	 * @return date
	 */
	public String getDate(){
		return this.date;
	}
}
