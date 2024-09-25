package edu.ndsu.cs.estimate.pages.estimation;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.cs.estimate.entities.interfaces.EstimationExercise;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationSuggestion;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationExerciseDatabaseService;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationSuggestionDatabaseService;

@RequiresRoles("admin")
public class ModeratorPage {

	@Inject 
	EstimationSuggestionDatabaseService suggestionDatabaseService;
	
	@Inject 
	EstimationExerciseDatabaseService exerciseDatabaseService;
	
	@Property
	List<? extends EstimationExercise> exercises;

	@Property
	List<? extends EstimationSuggestion> suggestions;
	
	@Property
	EstimationSuggestion suggestion;
	
	@Property
	EstimationExercise exercise;

	
	
	void setupRender() {
		suggestions = suggestionDatabaseService.listAllSuggestions();
	}
	
	void onApprove(int suggestionPK) {
		suggestion = suggestionDatabaseService.getSuggestion(suggestionPK);
		exercise = exerciseDatabaseService.getNewExerciseFromContext(suggestion.getCategories().get(0).getObjectContext());
		
		exercise.setName(suggestion.getName());
		exercise.setDescritption(suggestion.getDescription());
		exercise.addToCategories(suggestion.getCategories().get(0));
		exerciseDatabaseService.updateExercise(exercise);	
		suggestionDatabaseService.deleteSuggestion(suggestion.getPK());
	}
	
	void onDeny(int suggestionPK) {
		suggestionDatabaseService.deleteSuggestion(suggestionPK);
	}
	
}
