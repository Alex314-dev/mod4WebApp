package Model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import java.util.*;

@XmlRootElement
public class Email {
	private int id;
	private int event_id;
	private int person_id;
	private int organisation_id;
	private String organisation;
	
	private String senderEmail;
	private String subject;
	private String content;
	private List<Attachment> attachments = new ArrayList<>();
    private int priority;
    
    private String sendAt;
    private String sender;
	private String receiver;
	
	/**
	 * 
	 */
    public Email() {
        
    }
    
    /**
     * constructor with 12 attributes for email
     * @param id
     * @param event_id
     * @param person_id
     * @param organisation_id
     * @param senderEmail
     * @param subject
     * @param content
     * @param priority
     * @param sendAt
     * @param attachments
     * @param sender
     * @param organisation
     */
    public Email(int id, int event_id, int person_id, int organisation_id, String senderEmail, String subject,
			String content,  int priority, String sendAt, List<Attachment> attachments, String sender, String organisation) {

		this.id = id;
		this.event_id = event_id;
		this.person_id = person_id;
		this.organisation_id = organisation_id;
		this.senderEmail = senderEmail;
		this.subject = subject;
		this.content = content;
		this.attachments = attachments;
		this.priority = priority;
		this.sendAt = sendAt;
		this.sender = sender;
		this.organisation = organisation;
		
	}
	
    /**
     * constructor with 7 attributes for email
     * @param senderEmail
     * @param subject
     * @param content
     * @param sendAt
     * @param sender
     * @param attachments
     * @param receiver
     */
	public Email(String senderEmail, String subject,
			String content, String sendAt, String sender, List<Attachment> attachments, String receiver){
		
		this.senderEmail = senderEmail;
		this.subject = subject;
		this.content = content;
		this.sendAt = sendAt;
		this.sender = sender;
		this.attachments = attachments;
		this.receiver = receiver;
	}

// --------------- SETTERS & GETTERS --------------

	/**
	 * Returns the Id of the email
	 * @return Id of the email object
	 */
	public int getId() {
		return id;
	}
	/**
	 * Sets the Id of the email object
	 * @param id - Id of the email object
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Returns the Id of the event indicated in the email
	 * @return Id of the event
	 */
	public int getEvent_id() {
		return event_id;
	}
	/**
	 * 	Sets the Id of the event
	 * @param event_id - Id of the event
	 */
	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}
	/**
	 * Returns the Id of the receiver 
	 * @return Id of the receiver 
	 */
	public int getPerson_id() {
		return person_id;
	}
	/**
	 * Sets the person_id
	 * @param person_id - the Id of the receiver
	 */
	public void setPerson_id(int person_id) {
		this.person_id = person_id;
	}
	/**
	 * Returns the Id of the organisation the email is addressed to
	 * @return Id of the organisation 
	 */
	public int getOrganization_id() {
		return organisation_id;
	}
	
	/**
	 * set id of organisation
	 * @param organisation_id - Id of the organisation
	 */
	public void setOrganization_id(int organisation_id) {
		this.organisation_id = organisation_id;
	}
	
	/**
	 * get email of sender
	 * @return senderEmail
	 */
	public String getSenderEmail() {
		return senderEmail;
	}
	
	/**
	 * set email of sender
	 * @param senderEmail
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	
	/**
	 * get the subject of this email
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * set subject of this email	
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
		
	/**
	 * get the content of this email
	 * @return content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * set the content of this email
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * get all attachments of this email
	 * @return attachments - list of attachment objects
	 */
	public List<Attachment> getAttachments() {
		return attachments;
	}
	
	/**
	 * set a list of attachments for this email
	 * @param attachments
	 */
	public void setAttachments(ArrayList<Attachment> attachments) {
		this.attachments = attachments;
	}
	/**
	 * add one attachment 
	 * @param attachment
	 */
	public void addAttachment(Attachment attachment) {
		this.attachments.add(attachment);
	}
	
	/**
	 * get the priority of this email
	 * @return priority 
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * set the priority of this email 
	 * @param priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * get sendAt 
	 * @return sendAt - string which contains when the email was sent
	 */
	public String getSendAt() {
		return sendAt;
	}
	
	/**
	 * set when the email was sent
	 * @param sendat
	 */
	public void setSendAt(String sendat) {
		this.sendAt = sendat;
	}
	
	/**
	 * get the sender of this email
	 * @return sender
	 */
	public String getSender() {
		return sender;
	}
	
	/**
	 * set the sender of this email
	 * @param sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * get the organisation belonging to this email
	 * @return organisation
	 */
	public String getOrganisation() {
		return organisation;
	}
	/**
	 * set the organisation belonging to this email
	 * @param organisation
	 */
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	/**
	 * get receiver of the email
	 * @return receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * set receiver of the email
	 * @param receiver
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
    
    
}
