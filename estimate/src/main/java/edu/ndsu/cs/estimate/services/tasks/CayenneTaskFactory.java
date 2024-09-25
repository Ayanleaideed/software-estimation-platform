package edu.ndsu.cs.estimate.services.tasks;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;

public class CayenneTaskFactory {

	/* This adapts the existing factory class that was set up to work with the mock
	 *  objects by using that factory to create an instance and then just assigning
	 *  the values of that object to a CayenneProduct created from the supplied context. 
	 */
	public static void generateInstance(ObjectContext context, UserAccount newUser) {
		TaskInterface cayenneTask = newUser.getObjectContext().newObject(Task.class); 
		TaskInterface mockTask    = MockTaskFactory.generateInstance(newUser); 
		
		cayenneTask.setName(mockTask.getName());
		cayenneTask.setActualEndDate(mockTask.getActualEndDate());
		cayenneTask.setCompleted(mockTask.getCompleted());
		cayenneTask.setName(mockTask.getName());
		cayenneTask.setDropped(mockTask.getDropped());
		cayenneTask.setEstEndDate(mockTask.getEstEndDate());
		cayenneTask.setStartDate(mockTask.getStartDate());
		cayenneTask.setTimeTaken(mockTask.getTimeTaken());
		cayenneTask.setWillNotComplete(mockTask.getWillNotComplete());
		cayenneTask.setUser(mockTask.getUser());
	
		System.err.println("Task created... start date " + cayenneTask.getEstEndDate().toString() + "primary key: " + cayenneTask.getPK());
		newUser.getObjectContext().commitChanges();
	}
	
//	public static void generateInstances(int number, ObjectContext context) {
//		for(int i = 0; i < number; i++) {
//			generateInstance(context);
//		}
//	}
}

