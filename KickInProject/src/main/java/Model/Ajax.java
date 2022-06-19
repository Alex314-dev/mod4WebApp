package Model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Ajax {
	
	//
	public int draw;
	
	//
	public int recordsTotal;
	
	public int recordsFiltered;
	
	public List<Email> data;
	

	/**
	* empty constructor
	*/
	public Ajax() {
		
	}
	
		
	/**
	* 
	* @param emails
	* @param draw
	* @param recordsTotal
	* @param recordsFiltered
	*/
	public Ajax(List<Email> emails, int draw, int recordsTotal, int recordsFiltered) {
		this.data = emails;
		this.draw = draw;
		this.recordsTotal = recordsTotal;
		this.recordsFiltered = recordsFiltered;
	}
	
// --------------- SETTERS & GETTERS --------------

		
	/**
	 * method to get the draw
	 * @return draw
	 */
	public int getDraw() {
		return draw;
	}
	/**
	 * method to set the draw
	 * @param draw
	 */
	public void setDraw(int draw) {
		this.draw = draw;
	}
	/**
	 * method to get recordsTotal
	 * @return total records
	 */
	public int getRecordsTotal() {
		return recordsTotal;
	}
	/**
	 * method to set recordsTotal
	 * @param recordsTotal
	 */
	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	/**
	 * method to get recordsFiltered
	 * @return recordsFiltered
	 */
	public int getRecordsFiltered() {
		return recordsFiltered;
	}
	/**
	 * method to set recordsFiltered
	 * @param recordsFiltered
	 */
	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	/**
	 * method to get all data
	 * @return List<Email> data
	 */
	public List<Email> getData() {
		return data;
	}
	/**
	 * method to set data
	 * @param data
	 */
	public void setData(List<Email> data) {
		this.data = data;
	}
	
	
}
