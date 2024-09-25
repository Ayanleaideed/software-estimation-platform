package edu.ndsu.cs.estimate.entities.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationCategory;

public interface EstimationExercise {
	public Integer 	getPK(); 
	
	public String 	getName();
	public void	  	setName(String name);
	
	public String  	getDescritption();
	public void   	setDescritption(String description);

	public boolean  getOutcome();
	public void     setOutcome(boolean outcome);
	
	public boolean  getOutcomeReported();
	
	public void 	addToEstimates(EstimationEstimate estimation);
	
	public void addToCategories(EstimationCategory obj);
	public void removeFromCategories(EstimationCategory obj);
	
	public void setObjectContext(ObjectContext obj);
    public ObjectContext getObjectContext();
	
	
	
}
