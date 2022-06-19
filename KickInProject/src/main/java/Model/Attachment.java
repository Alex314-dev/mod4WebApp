package Model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Attachment {
	//id of attachment
	private  int id;
	
	//title of attachment
	private String title;

	//the attachments type e.g. pdf, png etc.
	private String type;
	
	/**
	 * empty constructor
	 */
	public Attachment() {
	}
	
	/**
	 * constructor to give attachment a title, type and id
	 * @param title
	 * @param type
	 * @param id
	 */
	public Attachment(String title, String type, int id) {
		this.title = title;
		this.type = type;
		this.id = id;
	}
	
// ------------ SETTERS & GETTERS --------------
	/**
	 * set the type
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * get the type
	 * @return type of the attachment
	 */
	public String getType() {
		return this.type;
	}
	/**
	 * set title
	 * @param title - the title of the attachment
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * get title
	 * @return title of this attachment
	 */
	public String getTitle() {
		return this.title;
	}
	/**
	 * set id
	 * @param id - Id of the attachment
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * get id
	 * @return Id of the attachment 
	 */
	public int getID() {
		return this.id;
	}
}