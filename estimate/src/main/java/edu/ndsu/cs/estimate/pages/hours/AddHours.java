package edu.ndsu.cs.estimate.pages.hours;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.cs.estimate.services.hours.HoursInterface;
import edu.ndsu.cs.estimate.services.tasks.TaskInterface;
import edu.ndsu.cs.estimate.cayenne.persistent.Hours;
import edu.ndsu.cs.estimate.pages.tasks.Index;
import edu.ndsu.cs.estimate.services.hours.CayenneHoursFactory;
import edu.ndsu.cs.estimate.services.hours.HoursDatabaseService;
import edu.ndsu.cs.estimate.services.tasks.TaskDatabaseService;

public class AddHours{

	@Inject
	private TaskDatabaseService taskDatabase;
	
	@Inject
	private HoursDatabaseService hoursDatabase;
	
	@Inject
	private AlertManager alertManager;
	
	@InjectComponent
	Form hoursForm;
	
	@Property
	@Persist
	private TaskInterface task;
	
	@Property
	private List<? extends HoursInterface> hours;
	
	@Property
	private HoursInterface hour;
	
	@Property
	private Integer taskPK;

	@Property
    @SessionAttribute
    private Date timestamp;
	
	void setupRender() {
		if (timestamp == null) {
			timestamp = new Date();
		}

		if (hour == null) {
			hour = new Hours();
		}


		if(taskPK != null) {
			task = taskDatabase.getTask(taskPK);
			//hours = hoursDatabase.listAllHoursByTask(task);
		}
		
	}
	
	void onValidateFromHoursForm() {
		List<String> errors = hour.validate();
		for(String error : errors) {
			hoursForm.recordError(error);
		}	
		if(!hoursForm.getHasErrors()) {
			hoursDatabase.updateHours(hour);
		}
	}
	
	Object onSuccessFromHoursForm() {
		alertManager.alert(Duration.TRANSIENT, Severity.SUCCESS, "Hours added successfully.");
		return Index.class;
	}
}

