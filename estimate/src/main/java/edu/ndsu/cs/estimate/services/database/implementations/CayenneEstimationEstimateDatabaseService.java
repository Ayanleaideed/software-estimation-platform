package edu.ndsu.cs.estimate.services.database.implementations;

import java.util.List;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;

import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationEstimate;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationEstimate;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationEstimateDatabaseService;

public class CayenneEstimationEstimateDatabaseService implements EstimationEstimateDatabaseService{
	private CayenneService cayenneService;
	
	public CayenneEstimationEstimateDatabaseService(CayenneService cayenneService)
	{
		this.cayenneService = cayenneService; 
	}
	
	@Override
	public List<? extends EstimationEstimate> listAllEstimates() {
		return ObjectSelect.query(CallibrationEstimate.class).select(cayenneService.newContext());
	}

	@Override
	public EstimationEstimate getEstimate(int PK) {
		return (EstimationEstimate) Cayenne.objectForPK(cayenneService.newContext(), CallibrationEstimate.class, PK);
	}

	@Override
	public EstimationEstimate getNewEstimate() {
		return (EstimationEstimate) cayenneService.newContext().newObject(CallibrationEstimate.class);
	}
	
	public EstimationEstimate getNewEstimateFromContext(ObjectContext newContext) {
		return (EstimationEstimate) newContext.newObject(CallibrationEstimate.class);
	}

	@Override
	public void deleteEstimate(int PK) {
		CallibrationEstimate estimate = (CallibrationEstimate) getEstimate(PK);
		ObjectContext context = estimate.getObjectContext();
		context.deleteObject(estimate);
		context.commitChanges();
	}

	@Override
	public void updateEstimate(EstimationEstimate estimate) {
		((CallibrationEstimate)estimate).getObjectContext().commitChanges();
	}


}
