package edu.ndsu.cs.estimate.cayenne.persistent;

import edu.ndsu.cs.estimate.cayenne.persistent.auto._CallibrationSuggestion;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationCategory;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationSuggestion;

public class CallibrationSuggestion extends _CallibrationSuggestion implements EstimationSuggestion {

    private static final long serialVersionUID = 1L;

	@Override
	public Integer getPK() {
		if(getObjectId() != null && !getObjectId().isTemporary())
	    {
	    		return (Integer) getObjectId().getIdSnapshot().get(SUGGESTION_ID_PK_COLUMN);
	    }
	    return null; 
	}

	@Override
	public void addToCategories(EstimationCategory obj) {
		addToCategories((CallibrationCategory) obj);
	}

	@Override
	public void removeFromCategories(EstimationCategory obj) {
		removeFromCategories((CallibrationCategory) obj);
	} 

}
