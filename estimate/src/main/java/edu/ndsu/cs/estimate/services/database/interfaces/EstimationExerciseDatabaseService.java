package edu.ndsu.cs.estimate.services.database.interfaces;

import java.util.List;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.entities.interfaces.EstimationExercise;

public interface EstimationExerciseDatabaseService {
	public List<? extends EstimationExercise> 	listAllExercises();  
	
	public EstimationExercise		getExercise(int PK);
	
	public EstimationExercise		getNewExercise(); 
	public EstimationExercise		getNewExerciseFromContext(ObjectContext context);
	
	public void						deleteExercise(int PK); 
	
	public void						updateExercise(EstimationExercise exercise);
	

}
