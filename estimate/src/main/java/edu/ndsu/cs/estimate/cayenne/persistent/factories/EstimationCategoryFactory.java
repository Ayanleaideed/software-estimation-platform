package edu.ndsu.cs.estimate.cayenne.persistent.factories;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationCategory;
import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationExercise;

public class EstimationCategoryFactory {
	
	public static void generateInstances(ObjectContext context)
	{
		String categories[] = {"Sports", "Elections", "Movies"};
		String categoryDescriptions[] = {"Predict the outcome of sports games from around the world", 
				"Predict the outcome of elections from around the world",
				"Predict the outcome of various new movies."};
		String events[][] = new String[3][3];
		String eventDescriptions[][] = new String[3][3];
		events[0][0] = "NDSU football 12/16";
		eventDescriptions[0][0] = "Will NDSU win the semi-finals game on 12/16/2022?";
		events[0][1] = "Vikings 12/18";
		eventDescriptions[0][1] = "Will the vikings win on 12/18/2022?";
		events[0][2] = "World cup USA";
		eventDescriptions[0][2] = "Will USA win the world cup?";
		
		events[1][0] = "USA Presidency";
		eventDescriptions[1][0] = "Will the local boy-Fred become President of the United States?";
		events[1][1] = "Argentina Secretary of State";
		eventDescriptions[1][1] = "Will Joe Smith become the Secretary of State?";
		events[1][2] = "Fargo City Council";
		eventDescriptions[1][2] = "Will group F become the city council?";
		
		events[2][0] = "Avatar 2 Grossing";
		eventDescriptions[2][0] = "Will Avatar 2 Gross more $ than the first Avatar?";
		events[2][1] = "Spider-Man: Beyond the Spider-Verse ratings";
		eventDescriptions[2][1] = "Will the newest Spiderman achieve a 90%+ rating on Rotten Tomatoes?";
		events[2][2] = "Cocaine Bear";
		eventDescriptions[2][2] = "Will Cocaine Bear become the best picture for 2023?";
		
		CallibrationCategory category[] = new CallibrationCategory[3];
		CallibrationExercise exercise[] = new CallibrationExercise[3];
		
		for(int i = 0; i < 3; i++)
		{
			category[i] = context.newObject(CallibrationCategory.class);
			category[i].setDescription(categoryDescriptions[i]);
			category[i].setName(categories[i]);
			for(int j = 0; j < 3; j++)
			{
				exercise[j] = context.newObject(CallibrationExercise.class);
				exercise[j].setName(events[i][j]);
				exercise[j].setDescritption(eventDescriptions[i][j]);
				category[i].addToExersises(exercise[j]);
			}
		}
		context.commitChanges();
		
		
		
	}
	
//	public static void generateInstances(ObjectContext context) {
//		for(int i = 0; i < number; i++) {
//			generateInstance(context);
//		}
//	}
}
