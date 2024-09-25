package edu.ndsu.cs.estimate.services.tasks;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.cs.estimate.cayenne.persistent.User;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.database.implementations.CayenneUserAccountDatabaseService;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;


public class MockTaskFactory {

private static Random rand = new Random();
	
	//@Inject
   // private static UserAccountDatabaseService accountService;
	private static String[] names1 = {"Gadgetron", "Thingamabopper", "WizBang", "Fantabulator"}; 
	private static String[] names2 = {"10", "20", "30", "40", "50"};
	private static String[] names3 = {"00", "20", "50", "55", "75", "00X", "50X", "50Z"}; 
	 @SuppressWarnings("deprecation")
	private static Date[] enddates = {new Date("12/5/2023"),new Date("12/1/2023"),new Date("10/20/2023"), new Date("9/7/2023")};
	 @SuppressWarnings("deprecation")
	private static Date[] sdates = {new Date("5/7/2023"),new Date("4/1/2023"),new Date("5/20/2023"), new Date("1/7/2023")};
//	private static List<? extends UserAccount> users = accountService.getUserAccounts(); 
	 
//	@SuppressWarnings("deprecation")
//	public static MockTask generateInstance() {
//		String 	name 		= oneOf(names1) + " " + oneOf(names2) + oneOf(names3); 
//		int		timeTaken 	= rand.nextInt(30) + 1;
//		Date	startDate = oneOf(sdates);
//		LocalDate	actualEndDate = LocalDate.now();
//		Date	estEndDate = oneOf(enddates);
//		boolean dropped = false;
//		boolean completed = false;
//		User user = (User) pickUser();
//		
//		
//		return new MockTask(name, timeTaken, startDate, estEndDate, actualEndDate, dropped, completed, user, willNotComplete); 
//	}
	
	public static MockTask generateInstance(UserAccount newUser) {
		String 	name 		= oneOf(names1) + " " + oneOf(names2) + oneOf(names3); 
		int		timeTaken 	= rand.nextInt(30) + 1;
		Date	startDate = oneOf(sdates);
		LocalDate	actualEndDate = LocalDate.now();
		Date	estEndDate = oneOf(enddates);
		boolean dropped = false;
		boolean completed = false;
		UserAccount user = newUser;
		boolean willNotComplete = false;
		
		
		return new MockTask(name, timeTaken, startDate, estEndDate, actualEndDate, dropped, completed, user, willNotComplete); 
	}
	

	private static<T> T oneOf(T[] values) {
		return values[rand.nextInt(values.length)];
	}
	
	/*private static UserAccount pickUser() {
		UserAccount temp;
		Random rn = new Random();
		int answer = rn.nextInt(1);
		temp = users.get(answer);
		return temp;
	}*/

}
