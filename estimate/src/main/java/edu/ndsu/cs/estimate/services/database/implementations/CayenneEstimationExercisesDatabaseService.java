package edu.ndsu.cs.estimate.services.database.implementations;

import java.util.List;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;

import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationEstimate;
import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationExercise;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationEstimate;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationExercise;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationExerciseDatabaseService;

public class CayenneEstimationExercisesDatabaseService implements EstimationExerciseDatabaseService{

	private CayenneService cayenneService;
	
	public CayenneEstimationExercisesDatabaseService(CayenneService cayenneService)
	{
		this.cayenneService = cayenneService;
	}
	
	@Override
	public List<? extends EstimationExercise> listAllExercises() {
		return ObjectSelect.query(CallibrationExercise.class).select(cayenneService.newContext());
	}

	@Override
	public EstimationExercise getExercise(int PK) {
		return (EstimationExercise) Cayenne.objectForPK(cayenneService.newContext(), CallibrationExercise.class, PK);
	}

	@Override
	public EstimationExercise getNewExercise() {
		return (EstimationExercise) cayenneService.newContext().newObject(CallibrationExercise.class);
	}
	
	public EstimationExercise getNewExerciseFromContext(ObjectContext context) {
		return (EstimationExercise) context.newObject(CallibrationExercise.class);
	}

	@Override
	public void deleteExercise(int PK) {
		CallibrationExercise exercise = (CallibrationExercise) getExercise(PK);
		ObjectContext context = exercise.getObjectContext();
		context.deleteObject(exercise);
		context.commitChanges();
	}

	@Override
	public void updateExercise(EstimationExercise exercise) {
		((CallibrationExercise)exercise).getObjectContext().commitChanges();
	}

	

}
