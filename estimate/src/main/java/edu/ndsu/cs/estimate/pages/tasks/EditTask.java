package edu.ndsu.cs.estimate.pages.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.cayenne.persistent.Hours;
import edu.ndsu.cs.estimate.services.tasks.TaskDatabaseService;
import edu.ndsu.cs.estimate.services.hours.HoursDatabaseService;

public class EditTask {

    @Inject
    private TaskDatabaseService taskDatabase;

    @Inject
    private HoursDatabaseService hoursDatabase;

    @InjectComponent
    private Form taskForm;

    @Inject
    private AlertManager alertManager;

    @Property
    @Persist
    private Task task;

    @Property
    private Integer taskPK;

    @Property
    private List<? extends Hours> hours;

    @Property
    private Hours hour;

    @Property
    private boolean noHours;
    
    @Property
	private String estEndDateStr;
    
    @Component(id = "estEndDateStr", parameters = { "value=estEndDateStr" })
	private TextField estEndDateStrField;

    void setupRender() {
        if (task != null) {
            hours = hoursDatabase.listAllHoursByTask(task);
            noHours = hours.isEmpty();
        } else {
            noHours = true;
        }
    }
    
    void onActivate(Integer taskPK) {
        this.taskPK = taskPK;
        if (taskPK != null) {
            task = taskDatabase.getTask(taskPK);
            if (task != null && task.getEstEndDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                estEndDateStr = dateFormat.format(task.getEstEndDate());
            }
        }
    }


    Integer onPassivate() {
        return taskPK;
    }

    void onValidateFromTaskForm() {
        if (task == null) {
            taskForm.recordError("Task is not specified.");
        }
        if (task.getName() != null) {
        	boolean taskNameValid = taskDatabase.isTaskNameValidEditing(task.getName(), task.getPK());
        	if (taskNameValid == false) {
        		taskForm.recordError("Task names must be unique.");
        	}
        }
        if (estEndDateStr != null) {
        	Date estEndDate = parseDate(estEndDateStr);
        	Date startDate = task.getStartDate();
        	
        	if(estEndDate == null) {
        		taskForm.recordError("Invalid date format. Please use MM/dd/yyyy.");
        	} else if (estEndDate.before(startDate)){
        		taskForm.recordError("Estimated end date must be after start date.");
        	} else {
        		task.setEstEndDate(estEndDate);
        		
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

    Object onSuccess() {
        if (task != null) {
            taskDatabase.updateTask(task);
            alertManager.alert(Duration.TRANSIENT, Severity.SUCCESS, "Task updated successfully.");
            return Index.class;
        } else {
            taskForm.recordError("Task update failed.");
            return null;
        }
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
