package edu.ndsu.cs.estimate.pages.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.cayenne.persistent.User;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;
import edu.ndsu.cs.estimate.services.tasks.TaskDatabaseService;

public class AddTask {

	@Inject
	TaskDatabaseService taskDatabase;

	@Property
	@Persist
	private Task task;

	@Inject
	private SecurityService securityService;

	@Inject
	private UserAccountDatabaseService userAccountDatabaseService;

	@Property
	@Persist
	private UserAccount userAccount;

	@Property
	private String startDateStr;

	@Property
	private String endDateStr;

	@Component(id = "startDateStr", parameters = { "value=startDateStr" })
	private TextField startDateStrField;

	@Component(id = "endDateStr", parameters = { "value=endDateStr" })
	private TextField endDateStrField;

	@Inject
	AlertManager alertManager;

	@InjectComponent
	private Form taskForm;

	@SessionAttribute
	@Property
	private Boolean noTasks = null;

	@SessionAttribute
	@Property
	private Boolean makeExamples = null;

	void setupRender() {
		if (noTasks == null) {
			noTasks = true;
		}
		if (makeExamples == null) {
			makeExamples = false;
		}
		task = taskDatabase.getNewTask();
		task.setDropped(false);
		task.setCompleted(false);
		task.setWillNotComplete(false);
		String principal = securityService.getSubject().getPrincipal().toString();
		userAccount = userAccountDatabaseService.getUserAccountFromContext(task.getObjectContext(), principal);
		task.setUser((User) userAccount);
	}

	Object onActionFromActionlink() {
		makeExamples = true; // Tell the tasks page to make demo tasks
		return edu.ndsu.cs.estimate.pages.tasks.Index.class; // Redirect to index
	}

	void onValidateFromTaskForm() {
		if (startDateStr == null || endDateStr == null) {
			taskForm.recordError("Dates cannot be null. Please use MM/dd/yyyy.");
		} else {
		    if (task.getPK() != null) {
		    	boolean taskNameValid = taskDatabase.isTaskNameValidAdding(task.getName());
		    	if (taskNameValid == false) {
		    		taskForm.recordError("Task names must be unique.");
		    	}
		    }
		    Date startDate = parseDate(startDateStr);
		    Date endDate = parseDate(endDateStr);
		    
			if (startDate == null || endDate == null) {
				taskForm.recordError("Invalid date format. Please use MM/dd/yyyy.");
			} else if (task.getName().length() > 25) {
				taskForm.recordError("Max Name Length Reached.");
			} else {
				task.setStartDate(startDate);
				task.setEstEndDate(endDate);
				
				List<String> errors = task.validate();
				for(String error : errors) {
					taskForm.recordError(error);
				}	
				if(!taskForm.getHasErrors()) {
					taskDatabase.updateTask(task);
				}
			}
		}
	}

	Object onSuccessFromTaskForm() {
		alertManager.alert(Duration.TRANSIENT, Severity.SUCCESS, "Task added successfully.");
		return Index.class;
	}

	private Date parseDate(String dateString) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			dateFormat.setLenient(false);
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

}
