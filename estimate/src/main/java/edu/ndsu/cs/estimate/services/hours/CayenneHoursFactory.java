package edu.ndsu.cs.estimate.services.hours;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.cayenne.persistent.Hours;
import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;

public class CayenneHoursFactory {

	/* This adapts the existing factory class that was set up to work with the mock
	 *  objects by using that factory to create an instance and then just assigning
	 *  the values of that object to a CayenneProduct created from the supplied context. 
	 */
	public static void generateInstance(ObjectContext context, Task newTask) {
		HoursInterface cayenneHours = newTask.getObjectContext().newObject(Hours.class);
		HoursInterface mockHours    = MockHoursFactory.generateInstance(newTask); 
		
		cayenneHours.setTask(mockHours.getTask());
		cayenneHours.setTimestamp(mockHours.getTimestamp());
		cayenneHours.setHoursLogged(mockHours.getHoursLogged());
	
		System.err.println("Hours created... timestamp " + cayenneHours.getTimestamp().toString() + " primary key: " + cayenneHours.getTask());
		newTask.getObjectContext().commitChanges();
	}
	
//	public static void generateInstances(int number, ObjectContext context) {
//		for(int i = 0; i < number; i++) {
//			generateInstance(context);
//		}
//	}
}

