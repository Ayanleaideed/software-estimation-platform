package edu.ndsu.cs.estimate.pages.estimation;

import java.util.List;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.cs.estimate.entities.interfaces.EstimationCategory;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationSuggestion;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationCategoryDatabaseService;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationSuggestionDatabaseService;

public class SuggestNewEvent {
	@Property
	private Integer categoryPK;
	
	@Inject 
	EstimationSuggestionDatabaseService suggestionDatabaseService;
	
	@Inject 
	EstimationCategoryDatabaseService categoryDatabaseService;
	
	@Property
	@Persist
	EstimationCategory category;
	
	@Property
	@Persist
	EstimationSuggestion suggestion;
	
	@InjectComponent
	private Form suggestionForm;
	
	void onActivate(Integer categoryPK) {
		this.categoryPK = categoryPK;
	}
	Integer onPassivate() {
		return categoryPK;
	}
	
	void setupRender() {
		if(categoryPK != null) {
			category = categoryDatabaseService.getCategory(categoryPK);
		}
		suggestion = suggestionDatabaseService.getNewSuggestionFromContext(category.getObjectContext());
	}
	
	void onValidateFromSuggestionForm() {
		List<String> errors = suggestion.validate();
		for(String error : errors) {
			suggestionForm.recordError(error);
		}
		
		if(errors.isEmpty()) {
//			suggestion.addToCategories(category);
			suggestionDatabaseService.updateSuggestion(suggestion);
			category.addToSuggestions(suggestion);
			categoryDatabaseService.updateCategory(category);
		}
	}
	
}
