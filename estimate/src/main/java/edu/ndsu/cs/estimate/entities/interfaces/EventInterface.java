package edu.ndsu.cs.estimate.entities.interfaces;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.cayenne.ObjectContext;

public interface EventInterface {
	public Integer 	getPK(); 

    public String 	getCategory();
	public void	  	setCategory(String category);

    public Date 	getCreatedDate();
	public void   	setCreatedDate(Date createdDate); 

    public String 	getDescription();
	public void   	setDescription(String description); 
	
	public Date 	getEventDate();
	public void 	setEventDate(Date eventDate);

    public int      getId();
	public void   	setId(int id); 

    public String   getName();
	public void   	setName(String name); 

    public int       getResult();
	public void   	 setResult(int result);

	public void setObjectContext(ObjectContext obj);
    public ObjectContext getObjectContext();

	@SuppressWarnings("deprecation")
	public default List<String> validate() {		
		ArrayList<String> errors = new ArrayList<String>();
		
		if(getName() == null || getName().trim().length() == 0) {
			errors.add("Name must be included.");
		} else if(getCategory() == null || getCategory().trim().length() == 0) {
			errors.add("Category must be included.");
		} else if(getDescription() == null || getDescription().trim().length() == 0) {
			errors.add("Description must be included.");
		} else if(getEventDate() == null) {
			errors.add("Event date must be included and in the format MM/dd/yyyy.");
		} else if(getEventDate().before(new Date(0, 0, 1))){
			errors.add("Date cannot be before 1/1/1900.");
		}

		return errors; 
	}
}
