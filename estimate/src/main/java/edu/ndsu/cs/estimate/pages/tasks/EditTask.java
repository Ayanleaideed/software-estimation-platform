package edu.ndsu.cs.estimate.pages.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
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
    
    @InjectComponent
    private Form editTimeStampForm;

    @Inject
    private AlertManager alertManager;

    @Property
    @Persist
    private TaskInterface task;
    @Property
    private Integer taskPK;

    @Property
    private List<? extends HoursInterface> taskHours;

    @Property
    private HoursInterface hour;

    @Property
    private boolean noHours;

	@Property
	private int editHours;
	
	@Property
	private String editTimeStamp;
    
    @Property
	private String estEndDateStr;
    
    void setupRender() {
        if (taskPK != null) {
            task = taskDatabase.getTask(taskPK);
            if (task != null) {
                getHours();
            } else {
                noHours = true;
            }
        } else {
            noHours = true;
        }
    }

    private void getHours() {
        taskHours = hoursDatabase.listAllHoursByTask(task);
        noHours = taskHours.isEmpty();
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

    void onSubmitFromDeleteForm(int pk) {
        // Delete the hour entry
        hoursDatabase.deleteHours(pk);

        // Refresh displayed hours
        getHours();
    }

    void onSubmitFromEditForm(int pk) {
        HoursInterface tempHours = hoursDatabase.getHours(pk);
        tempHours.setHoursLogged(editHours);
        hoursDatabase.updateHours(tempHours);

        // Reset editHours
        editHours = 0;

        // Refresh displayed hours
        getHours();
    }
    
    void onSubmitFromEditTimeStampForm(int pk) {
        HoursInterface tempHours = hoursDatabase.getHours(pk);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        //SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        dateFormat.setLenient(false);
        if (editTimeStamp != null && !editTimeStamp.isEmpty()) {
        	try {
        		Date timeStamp = dateFormat.parse(editTimeStamp);
        		tempHours.setTimestamp(timeStamp);
        		hoursDatabase.updateHours(tempHours);
        	} catch (ParseException e) {
        		editTimeStampForm.recordError("The date format is invalid. Please use MM/dd/yyyy.");
        		return;
        	}
        }

        // Reset editHours
        editTimeStamp = "";

        // Refresh displayed hours
        getHours();
    }
}
