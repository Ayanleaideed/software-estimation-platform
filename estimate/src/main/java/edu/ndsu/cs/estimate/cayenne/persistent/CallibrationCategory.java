package edu.ndsu.cs.estimate.cayenne.persistent;

import edu.ndsu.cs.estimate.cayenne.persistent.auto._CallibrationCategory;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationCategory;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationSuggestion;

public class CallibrationCategory extends _CallibrationCategory implements EstimationCategory {

    private static final long serialVersionUID = 1L;

	@Override
	public Integer getPK() {
 	if(getObjectId() != null && !getObjectId().isTemporary())
    {
    		return (Integer) getObjectId().getIdSnapshot().get(CATEGORY_ID_PK_COLUMN);
    }
    	return null; 
    }

	@Override
	public void addToSuggestions(EstimationSuggestion obj) {
		addToSuggestions((CallibrationSuggestion) obj);
	}

	@Override
	public void removeFromSuggestions(EstimationSuggestion obj) {
		removeFromSuggestions((CallibrationSuggestion) obj);
	}

}
