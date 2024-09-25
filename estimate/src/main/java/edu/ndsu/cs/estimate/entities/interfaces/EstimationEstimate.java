package edu.ndsu.cs.estimate.entities.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.cayenne.persistent.User;

public interface EstimationEstimate {
	public Integer 	getPK(); 
	
	public int 		getPercentPrediction();
	public void	  	setPercentPrediction(int percentPrediction);
	
	public void setExersise(EstimationExercise exersise);
    public EstimationExercise getExersise();

	public void setUser(User user);
    public User getUser();
    
    public void setObjectContext(ObjectContext obj);
    public ObjectContext getObjectContext();
	
	public default List<String> 		validate() {
		ArrayList<String> errors = new ArrayList<String>();
		if(getPercentPrediction() < 0 || getPercentPrediction() > 100)
		{
			errors.add("The prediction has to be between 0 and 100 percent!");
		}
		return errors;
	}
	
		
	
}
