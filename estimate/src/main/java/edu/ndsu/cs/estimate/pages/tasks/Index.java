package edu.ndsu.cs.estimate.pages.tasks;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;
import edu.ndsu.cs.estimate.services.tasks.CayenneTaskFactory;
import edu.ndsu.cs.estimate.services.tasks.TaskDatabaseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Index {
	
	@Inject
    private AlertManager alertManager;

    @Property
    @Persist
    private String dateRange;

    @InjectComponent
    private Form dateForm;

    @Component(id = "dateRange", parameters = {"value=dateRange"})
    private TextField dateRangeField;
    
	@Persist
	@Property
	private int hours;
	
    @Inject
    private TaskDatabaseService taskDBS;

    @Persist
    @Property
    private List<? extends Task> tasks;

    @Property
    private Task task;

    @InjectComponent
    private Form addHourForm;
    
    @InjectComponent
    private Form add1HourForm;
    
    @InjectComponent
    private Form add2HourForm;
    
    @InjectComponent
    private Form add3HourForm;
    
    @InjectComponent
    private Form add5HourForm;
    
    @InjectComponent
    private Form add10HourForm;

    @Inject
    private SecurityService securityService;

    @Inject
    private UserAccountDatabaseService userAccountDatabaseService;

    @Property
    @Persist
    private UserAccount userAccount;
    
    @SessionAttribute
    @Property
    private Boolean noTasks = null;
     
    @SessionAttribute
    @Property
    private Boolean makeExamples = null;


    @SetupRender
    void setupRender() {
        if (noTasks == null) {
            noTasks = true;
        }
        if (makeExamples == null) {
            makeExamples = false;
        }
        if (securityService.getSubject().getPrincipal() == null ) {
        	return;  //User needs to log in
        }
        //alerts if tasks are due within the next week
        List<String> alerts = taskDBS.getDeadlineNotifications();
        if (alerts.size() > 0) {
        	String alertString = "";
    		for(String alert : alerts) {
    			alertString += alert + "\n";
    		}
            alertManager.alert(Duration.SINGLE, Severity.INFO, alertString);
        } 
        
        String principal = securityService.getSubject().getPrincipal().toString();
        userAccount = userAccountDatabaseService.getUserAccount(principal);
        getTasks();
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
        if (makeExamples) makeExampleTasks();
        tasks = taskDBS.listAllTasks(start, end, userAccount);
        noTasks = tasks.isEmpty();
        return;
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
    	getTasks(); //Update displayed tasks from range
    }

	
	void onDelete(int PK) {
		taskDBS.deleteTask(PK);
		getTasks(); //Update displayed tasks
	}
	
	void onSubmitFromAddHourForm(int pk) {
		Task tempTask = taskDBS.getTask(pk);
		int temp = tempTask.getTimeTaken() + this.hours;
		tempTask.setTimeTaken(temp);
		taskDBS.updateTask(tempTask);
		getTasks(); //Update displayed tasks
	}
	
	void onSubmitFromAdd1HourForm(int pk) {
		Task tempTask = taskDBS.getTask(pk);
		int temp = tempTask.getTimeTaken() + (int)1;
		tempTask.setTimeTaken(temp);
		taskDBS.updateTask(tempTask);
		getTasks(); //Update displayed tasks
	}
	
	void onSubmitFromAdd2HourForm(int pk) {
		Task tempTask = taskDBS.getTask(pk);
		int temp = tempTask.getTimeTaken() + (int)2;
		tempTask.setTimeTaken(temp);
		taskDBS.updateTask(tempTask);
		getTasks(); //Update displayed tasks
	}
	
	void onSubmitFromAdd3HourForm(int pk) {
		Task tempTask = taskDBS.getTask(pk);
		int temp = tempTask.getTimeTaken() + (int)3;
		tempTask.setTimeTaken(temp);
		taskDBS.updateTask(tempTask);
		getTasks(); //Update displayed tasks
	}
	
	void onSubmitFromAdd5HourForm(int pk) {
		Task tempTask = taskDBS.getTask(pk);
		int temp = tempTask.getTimeTaken() + (int)5;
		tempTask.setTimeTaken(temp);
		taskDBS.updateTask(tempTask);
		getTasks(); //Update displayed tasks
	}
	
	void onSubmitFromAdd10HourForm(int pk) {
		Task tempTask = taskDBS.getTask(pk);
		int temp = tempTask.getTimeTaken() + (int)10;
		tempTask.setTimeTaken(temp);
		taskDBS.updateTask(tempTask);
		getTasks(); //Update displayed tasks
	}
	
	void makeExampleTasks() {
		makeExamples = false;
        for (int i = 0; i < 5; i++) {
            CayenneTaskFactory.generateInstance(taskDBS.getCayenneService().newContext(), userAccount);
        }
        getTasks(); //Update displayed tasks after adding
        System.err.println("Amount of tasks now " + tasks.size());
	}
	
	@OnEvent(component="complete")
	Object onClickCloseComplete(int pk) {
			Task tempTask = taskDBS.getTask(pk);
			tempTask.setCompleted(true);
			taskDBS.updateTask(tempTask);
		return Index.class;
	}
	@OnEvent(component="drop")
	Object onClickCloseDropped(int pk) {
			Task tempTask = taskDBS.getTask(pk);
			tempTask.setDropped(true);
			taskDBS.updateTask(tempTask);
		return Index.class;
	}
	
	@OnEvent(component="willNotComplete")
	Object onClickCloseWillNotComplete(int pk) {
			Task tempTask = taskDBS.getTask(pk);
			tempTask.setWillNotComplete(true);
			taskDBS.updateTask(tempTask);
		return Index.class;
	}
}
