package edu.ndsu.cs.estimate.services.database.interfaces;

import java.util.List;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.entities.interfaces.EstimationSuggestion;

public interface EstimationSuggestionDatabaseService {
	public List<? extends EstimationSuggestion> 	listAllSuggestions();  
	
	public EstimationSuggestion		getSuggestion(int PK);
	
	public EstimationSuggestion		getNewSuggestion();
	
	public EstimationSuggestion 	getNewSuggestionFromContext(ObjectContext newContext);
	
	public void						deleteSuggestion(int PK); 
	
	public void						updateSuggestion(EstimationSuggestion exercise);
}
