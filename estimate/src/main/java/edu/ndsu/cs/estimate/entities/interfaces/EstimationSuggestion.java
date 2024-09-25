package edu.ndsu.cs.estimate.entities.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationCategory;



public interface EstimationSuggestion {
	public Integer 	getPK(); 
	
	public String 	getName();
	public void	  	setName(String name);
	
	public String  	getDescription();
	public void   	setDescription(String description);
	
	public void 	addToCategories(EstimationCategory obj);
	public void 	removeFromCategories(EstimationCategory obj);
	public List<? extends EstimationCategory> getCategories();
	
	public void setObjectContext(ObjectContext obj);
    public ObjectContext getObjectContext();
    
    public default List<String> 		validate() {
		ArrayList<String> errors = new ArrayList<String>();
		if(getName() == null || getName().trim().length() == 0) {
			errors.add("Name must be included.");
		}
		else if(getName().length() > 128) {
			errors.add("Name cannot contain more than 2048 characters."); 
		}
		if(getDescription() == null || getDescription().trim().length() == 0) {
			errors.add("Description must be included.");
		}
		else if(getDescription().length() > 2048) {
			errors.add("Description cannot contain more than 2048 characters."); 
		}
		return errors;
	}
	
	
}
