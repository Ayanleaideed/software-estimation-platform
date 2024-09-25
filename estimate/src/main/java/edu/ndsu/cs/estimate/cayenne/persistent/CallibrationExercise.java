package edu.ndsu.cs.estimate.cayenne.persistent;

import edu.ndsu.cs.estimate.cayenne.persistent.auto._CallibrationExercise;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationCategory;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationEstimate;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationExercise;

public class CallibrationExercise extends _CallibrationExercise implements EstimationExercise{

    private static final long serialVersionUID = 1L; 

    public Integer getPK()
    {
    	if(getObjectId() != null && !getObjectId().isTemporary())
    	{
    		return (Integer) getObjectId().getIdSnapshot().get(EXERSISE_ID_PK_COLUMN);
    	}
    	return null; 
    }

	@Override
	public void addToEstimates(EstimationEstimate estimation) {
		addToEstimates((CallibrationEstimate) estimation);
	}

	@Override
	public void addToCategories(EstimationCategory obj) {
		addToCategories((CallibrationCategory) obj);
	}

	@Override
	public void removeFromCategories(EstimationCategory obj) {
		removeFromCategories((CallibrationCategory) obj);
	}
	
	@Override
	public void setOutcome(boolean outcome)
	{
		setOutcomeReported(true);
		beforePropertyWrite("outcome", this.outcome, outcome);
        this.outcome = outcome;
	}
	
	public boolean getOutcome() {
		return this.isOutcome();
	}
	
	public boolean getOutcomeReported() {
		return this.isOutcomeReported();
	}

}
