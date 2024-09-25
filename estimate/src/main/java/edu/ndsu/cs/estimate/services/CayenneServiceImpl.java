package edu.ndsu.cs.estimate.services;


import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;

import edu.ndsu.cs.estimate.cayenne.persistent.*;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;

import java.util.ArrayList;


public class CayenneServiceImpl implements CayenneService {

	private ServerRuntime cayenneRuntime;
	
	public CayenneServiceImpl() {
		cayenneRuntime = ServerRuntime.builder().addConfig("cayenne-estimate.xml").build();
		ObjectContext context = cayenneRuntime.newContext();
		ArrayList<CallibrationExercise> exercises = new ArrayList<CallibrationExercise>();
		ArrayList<CallibrationExercise> categories = new ArrayList<CallibrationExercise>(); 
		ArrayList<Hours> hours = new ArrayList<Hours>();
		 		
		for(int i = 0; i < 2; i++) {
			exercises.add(getCallibrationExerciseInstance(context, i));
		 	
		}

		context.commitChanges();
	}
	
	private CallibrationExercise getCallibrationExerciseInstance(ObjectContext context, int index) {
		String[] names = {"Bears game", "Election"};
		String[] descriptions  = {"Will the bears win the game Tuesday?", "Will Billy be elected as emperor?"};
		CallibrationExercise exercise = context.newObject(CallibrationExercise.class);
		exercise.setName(names[index]);
		exercise.setDescritption(descriptions[index]);
			
		return exercise; 
	}


	@Override
	public ObjectContext newContext() {
		return cayenneRuntime.newContext();
	}	
}



