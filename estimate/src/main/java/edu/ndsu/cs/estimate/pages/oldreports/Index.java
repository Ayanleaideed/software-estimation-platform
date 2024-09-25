package edu.ndsu.cs.estimate.pages.oldreports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.cayenne.persistent.User;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;
import edu.ndsu.cs.estimate.services.tasks.TaskDatabaseService;

public class Index {
	@Inject
	private TaskDatabaseService taskDBS;
	
	@Property
	private List<? extends Task> tasks;
	@SuppressWarnings("deprecation")
	@Property
	@Persist
	private Date start; 
	
	@SuppressWarnings("deprecation")
	@Property 
	@Persist
	private Date end;
	
    @Property
    @Persist
    private String dateRange;
    
    @InjectComponent
    private Form dateForm;
    
    @Component(id = "dateRange", parameters = {"value=dateRange"})
    private TextField dateRangeField;
	
	@Property
	private Task task; 
	int size;
	
	@Inject
	private SecurityService securityService;

	@Inject
	private UserAccountDatabaseService userAccountDatabaseService;
	
	@Property
	@Persist
	private UserAccount	userAccount;
	
	@Property
	private boolean noTasks;

	
	@SetupRender
	void setupRender() {
        if (securityService.getSubject().getPrincipal() == null ) {
        	return;  //User needs to log in
        }
		String principal = securityService.getSubject().getPrincipal().toString(); 
		userAccount = userAccountDatabaseService.getUserAccount(principal);
		getTasks();
		size = tasks.size();
	}
	
    private void getTasks() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);
        Date start = new Date(0); // 1/1/1970
        // 0 ms after 1/1/1970
        Date end = new Date(2145916800000L); // 1/1/2038
        // 2145916800000 ms after 1/1/1970, L for long input, instead of int

        if (dateRange != null && !dateRange.isEmpty()) {
            String[] dates = dateRange.split(" - ");
            try {
                start = dateFormat.parse(dates[0]);
                // If there is an end date, parse it; otherwise, use the start date for both
                end = (dates.length == 2) ? dateFormat.parse(dates[1]) : start;
            } catch (ParseException e) {
                dateForm.recordError("The date range format is invalid.");
                return;
            }
        }

        tasks = taskDBS.listAllTasks(start, end, userAccount); // Fetch tasks based on date range
        noTasks = tasks.isEmpty();
    }
	
    void onValidateFromDateForm() {
        if (dateRange != null && !dateRange.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            dateFormat.setLenient(false);
            String[] dates = dateRange.split(" - ");
            if (dates.length == 2) {
                try {
                    dateFormat.parse(dates[0]);
                    dateFormat.parse(dates[1]);
                } catch (ParseException e) {
                    dateForm.recordError("The date range format is invalid. Please use MM/dd/yyyy - MM/dd/yyyy format.");
                }
            } else {
                dateForm.recordError("The date range format is invalid. Please use MM/dd/yyyy - MM/dd/yyyy format.");
            }
        }
    }
    
    void onSuccessFromDateForm() {
    	getTasks();
    }
	
	public long returnDifferenceEstimated(Task task) {
		java.util.Date TaskStartDate = task.getStartDate();
		System.out.println("Start Date" + task.getStartDate() + "| End Date : " + task.getEstEndDate());
		java.util.Date TaskExpectedEndDate = task.getEstEndDate();
		
		long StartExpectDiffInMilli = Math.abs(TaskExpectedEndDate.getTime() - TaskStartDate.getTime());
		long StartExpectDiffInDays = TimeUnit.DAYS.convert(StartExpectDiffInMilli, TimeUnit.MILLISECONDS);
		
		return StartExpectDiffInDays;
	}
	public long returnDifferenceActual(Task task) {
		java.util.Date TaskStartDate = task.getStartDate();
		LocalDate holderDate = task.getActualEndDate();
		java.util.Date TaskActualEndDate= Date.from(holderDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		long StartActualDiffInMilli = Math.abs(TaskActualEndDate.getTime() - TaskStartDate.getTime());
		long StartActualtDiffInDays = TimeUnit.DAYS.convert(StartActualDiffInMilli, TimeUnit.MILLISECONDS);
		
		return StartActualtDiffInDays ;
	}
	
	//Most likely will not use, can probably be removed later
	public void recreateGraph() {
		System.out.println("hello two");
		Date start = new Date("12/1/2022");
		Date end = new Date("12/9/2022");
		tasks = taskDBS.listAllTasks(start,end, (User)this.userAccount);
		size = tasks.size();
		Object[][] graphArray = new Object[size][3];
		graphArray[0][0] = " ";
		graphArray[0][1] = "Expected Days";
		graphArray[0][2] = "Actual Days";
		int currentPointer = 1;
		System.out.println(tasks.toString());
		System.out.println(graphArray[0][1]);
		for (Task currentTask : tasks) {
			String TaskName = currentTask.getName();
			java.util.Date TaskStartDate = currentTask.getStartDate();
			java.util.Date TaskExpectedEndDate = currentTask.getEstEndDate();
			LocalDate holderDate = currentTask.getActualEndDate();
			java.util.Date TaskActualEndDate= Date.from(holderDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			
			long StartExpectDiffInMilli = Math.abs(TaskExpectedEndDate.getTime() - TaskStartDate.getTime());
			long StartExpectDiffInDays = TimeUnit.DAYS.convert(StartExpectDiffInMilli, TimeUnit.MILLISECONDS);
			
			long StartActualDiffInMilli = Math.abs(TaskActualEndDate.getTime() - TaskStartDate.getTime());
			long StartActualDiffInDays = TimeUnit.DAYS.convert(StartActualDiffInMilli, TimeUnit.MILLISECONDS);
			graphArray[currentPointer][0] =TaskName;
			graphArray[currentPointer][1] = StartExpectDiffInDays;
			graphArray[currentPointer][2] = StartActualDiffInDays;
			System.out.println(graphArray[currentPointer][0] + " " + graphArray[currentPointer][1] + " " + graphArray[currentPointer][2]);	
		}
	}
}
