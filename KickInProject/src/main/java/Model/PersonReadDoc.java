package Model;

public class PersonReadDoc {
    private String personEmail;
    private String date;

   /**
     * constructor which adds email and date to PersonReadDoc object
     * @param personEmail
     * @param date
     */
	public PersonReadDoc(String personEmail, String date){
        this.personEmail = personEmail;
        this.date = date;
    }
    /**
     * constructor which adds email, date and organisation to PersonReadDoc object
     * @param personEmail
     * @param date
     * @param organisation
     */
    public PersonReadDoc(String personEmail, String date, String organisation){
        this.personEmail = personEmail;
        this.date = date;
    }

// --------------- GETTERS --------------
    /**
     * get email of the person who read the doc
     * @return personEmail
     */
    public String getPersonEmail(){
        return this.personEmail;
    }
    /**
     * get the date of when the person read the doc
     * @return date
     */
    public String getDate(){
        return this.date;
    }
}