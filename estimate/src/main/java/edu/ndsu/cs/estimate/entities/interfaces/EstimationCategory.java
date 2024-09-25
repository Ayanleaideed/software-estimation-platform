package edu.ndsu.cs.estimate.entities.interfaces;

import java.util.List;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationExercise;


public interface EstimationCategory {
    public Integer 	getPK(); 
	
	public String 	getName();
	public void	  	setName(String name);
	
	public String  	getDescription();
	public void   	setDescription(String description);
	
	public void setObjectContext(ObjectContext obj);
    public ObjectContext getObjectContext();
	
	public List<CallibrationExercise> getExersises();
	
	public void addToSuggestions(EstimationSuggestion obj);
	public void removeFromSuggestions(EstimationSuggestion obj);
	
}
