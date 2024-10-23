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

import edu.ndsu.cs.estimate.services.tasks.TaskInterface;
import edu.ndsu.cs.estimate.services.hours.HoursInterface;
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
    private TaskInterface task;

    @Property
    private Integer taskPK;

    @Property
    private List<? extends HoursInterface> hours;

    @Property
    private HoursInterface hour;

    @Property
    private boolean noHours;
    
    @Property
	private String estEndDateStr;
    

    void setupRender() {
    	if(taskPK != null) {
			task = taskDatabase.getTask(taskPK);
		}
    	
        //if (task != null) {
        //   hours = hoursDatabase.listAllHoursByTask(task);
        //    noHours = hours.isEmpty();
        //} else {
        //    noHours = true;
        //}
    }
    
    void onActivate(Integer taskPK) {
    	this.taskPK = taskPK; 
    }


    Integer onPassivate() {
        return taskPK;
    }

    void onValidateFromTaskForm() {
    	List<String> errors = task.validate();
		for(String error : errors) {
			taskForm.recordError(error);
		}	
		if(!taskForm.getHasErrors()) {
			taskDatabase.updateTask(task);
		}
    }

    Object onSuccessFromTaskForm() {
		alertManager.alert(Duration.TRANSIENT, Severity.SUCCESS, "Task added successfully.");
		return Index.class;
	}

}
