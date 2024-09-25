package edu.ndsu.cs.estimate.pages.estimation;

import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.cs.estimate.entities.interfaces.EstimationCategory;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationExercise;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationCategoryDatabaseService;

public class ViewExercises {
	
	@Inject 
	EstimationCategoryDatabaseService categoryDatabaseService;
	
	@Property
	private Integer categoryPK;
	
	@Property
	List<? extends EstimationExercise> exercises;
	
	@Property
	EstimationExercise exercise;
	
	@Property
	private EstimationCategory category;
	
	void onActivate(Integer categoryPK) {
		this.categoryPK = categoryPK;
	}
	Integer onPassivate() {
		return categoryPK;
	}
	
	void setupRender() {
		if(categoryPK != null) {
			category = categoryDatabaseService.getCategory(categoryPK);
			exercises = category.getExersises();
			for(int i = 0; i < exercises.size(); i++)
			{
				if(exercises.get(i).getOutcomeReported())
				{
					exercises.remove(i);
				}
			}
		}
	}
}
