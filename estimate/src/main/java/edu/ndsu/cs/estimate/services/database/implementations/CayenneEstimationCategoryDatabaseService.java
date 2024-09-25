package edu.ndsu.cs.estimate.services.database.implementations;

import java.util.List;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;

import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationCategory;
import edu.ndsu.cs.estimate.cayenne.persistent.factories.EstimationCategoryFactory;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationCategory;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationCategoryDatabaseService;

public class CayenneEstimationCategoryDatabaseService implements EstimationCategoryDatabaseService{

	private CayenneService cayenneService;
	
	public CayenneEstimationCategoryDatabaseService(CayenneService cayenneService)
	{
		this.cayenneService = cayenneService; 
		
		EstimationCategoryFactory.generateInstances(cayenneService.newContext());
	}
	@Override
	public List<? extends EstimationCategory> listAllCategories() {
		return ObjectSelect.query(CallibrationCategory.class).select(cayenneService.newContext());
	}

	@Override
	public EstimationCategory getCategory(int PK) {
		return (EstimationCategory) Cayenne.objectForPK(cayenneService.newContext(), CallibrationCategory.class, PK);
	}

	@Override
	public EstimationCategory getNewCategory() {
		return (EstimationCategory) cayenneService.newContext().newObject(CallibrationCategory.class);
	}

	@Override
	public void deleteCategory(int PK) {
		CallibrationCategory category = (CallibrationCategory) getCategory(PK);
		ObjectContext context = category.getObjectContext();
		context.deleteObject(category);
		context.commitChanges();
	}

	@Override
	public void updateCategory(EstimationCategory category) {
		((CallibrationCategory)category).getObjectContext().commitChanges();
	}

}
