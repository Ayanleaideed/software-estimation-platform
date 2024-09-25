package edu.ndsu.cs.estimate.services.database.interfaces;

import java.util.List;

import edu.ndsu.cs.estimate.entities.interfaces.EstimationCategory;

public interface EstimationCategoryDatabaseService {
	public List<? extends EstimationCategory> 	listAllCategories();  
	
	public EstimationCategory		getCategory(int PK);
	
	public EstimationCategory		getNewCategory(); 
	
	public void						deleteCategory(int PK); 
	
	public void						updateCategory(EstimationCategory category);
}
