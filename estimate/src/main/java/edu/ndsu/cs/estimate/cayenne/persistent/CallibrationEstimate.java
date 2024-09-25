package edu.ndsu.cs.estimate.cayenne.persistent;

import edu.ndsu.cs.estimate.cayenne.persistent.auto._CallibrationEstimate;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationEstimate;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationExercise;

public class CallibrationEstimate extends _CallibrationEstimate implements EstimationEstimate{

    private static final long serialVersionUID = 1L; 

    @Override
	public Integer getPK() {
	 	if(getObjectId() != null && !getObjectId().isTemporary())
	    {
	    		return (Integer) getObjectId().getIdSnapshot().get(ESTIMATE_ID_PK_COLUMN);
	    }
	    return null; 
    }

	@Override
	public void setExersise(EstimationExercise exersise) {
		setExersise((CallibrationExercise) exersise);
	}
}
