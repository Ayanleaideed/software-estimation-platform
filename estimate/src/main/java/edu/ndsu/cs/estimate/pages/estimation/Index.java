package edu.ndsu.cs.estimate.pages.estimation;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.cs.estimate.entities.interfaces.EstimationCategory;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationEstimate;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationCategoryDatabaseService;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationEstimateDatabaseService;

import java.util.List;

public class Index {
	
	@Property
	List<? extends EstimationCategory> categories;
	
	@Property
	EstimationCategory category;
	
	@Property
	List<? extends EstimationEstimate> estimations;
	
	@Property
	EstimationEstimate estimation;
	
	@Inject 
	EstimationCategoryDatabaseService categoryDatabaseService;

	@Inject 
	EstimationEstimateDatabaseService estimateDatabaseService;
	
	void setupRender() {
		categories = categoryDatabaseService.listAllCategories();
		estimations = estimateDatabaseService.listAllEstimates();
	}
	
	void onDelete(int PK) {
		categoryDatabaseService.deleteCategory(PK);
	}
	
}
