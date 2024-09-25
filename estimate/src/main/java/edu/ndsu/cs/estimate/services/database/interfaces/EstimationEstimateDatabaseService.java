package edu.ndsu.cs.estimate.services.database.interfaces;

import java.util.List;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationEstimate;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationEstimate;

public interface EstimationEstimateDatabaseService {
	public List<? extends EstimationEstimate> 	listAllEstimates();  
	
	public EstimationEstimate		getEstimate(int PK);
	
	public EstimationEstimate		getNewEstimate(); 
	
	public EstimationEstimate		getNewEstimateFromContext(ObjectContext newContext);
	
	public void						deleteEstimate(int PK); 
	
	public void						updateEstimate(EstimationEstimate estimate);
}
