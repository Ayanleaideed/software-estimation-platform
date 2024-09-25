package edu.ndsu.cs.estimate.services.database.implementations;

import java.util.List;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;

import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationEstimate;
import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationSuggestion;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationEstimate;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationSuggestion;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationSuggestionDatabaseService;

public class CayenneEstimationSuggestionDatabaseService implements EstimationSuggestionDatabaseService{
	private CayenneService cayenneService;
	
	public CayenneEstimationSuggestionDatabaseService(CayenneService cayenneService)
	{
		this.cayenneService = cayenneService;
	}
	
	@Override
	public List<? extends EstimationSuggestion> listAllSuggestions() {
		return ObjectSelect.query(CallibrationSuggestion.class).select(cayenneService.newContext());
	}

	@Override
	public EstimationSuggestion getSuggestion(int PK) {
		return (EstimationSuggestion) Cayenne.objectForPK(cayenneService.newContext(), CallibrationSuggestion.class, PK);
	}

	@Override
	public EstimationSuggestion getNewSuggestion() {
		return (EstimationSuggestion) cayenneService.newContext().newObject(CallibrationSuggestion.class);
	}
	
	public EstimationSuggestion getNewSuggestionFromContext(ObjectContext newContext) {
		return (EstimationSuggestion) newContext.newObject(CallibrationSuggestion.class);
	}

	@Override
	public void deleteSuggestion(int PK) {
		CallibrationSuggestion suggestion = (CallibrationSuggestion) getSuggestion(PK);
		ObjectContext context = suggestion.getObjectContext();
		context.deleteObject(suggestion);
		context.commitChanges();
	}

	@Override
	public void updateSuggestion(EstimationSuggestion suggestion) {
		((CallibrationSuggestion)suggestion).getObjectContext().commitChanges();
	}
}
