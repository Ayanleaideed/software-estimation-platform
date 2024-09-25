package edu.ndsu.cs.estimate.services.hours;

import java.util.Date;
import java.util.Random;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;


public class MockHoursFactory {

private static Random rand = new Random(); 
	
	public static MockHours generateInstance(Task newTask) {
		Task 	task			= newTask;
		System.out.print(task.getPK());
		Date 	timestamp 		= new Date();
		System.out.println(timestamp);
		int		hoursLogged 	= rand.nextInt(30) + 1;
		
		return new MockHours(task, timestamp, hoursLogged); 
	}


}
