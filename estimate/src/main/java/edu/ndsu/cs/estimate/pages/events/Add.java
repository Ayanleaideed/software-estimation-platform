package edu.ndsu.cs.estimate.pages.events;

import edu.ndsu.cs.estimate.cayenne.persistent.Event;
import edu.ndsu.cs.estimate.cayenne.persistent.User;
import edu.ndsu.cs.estimate.entities.interfaces.EventInterface;
import edu.ndsu.cs.estimate.services.database.interfaces.EventDatabaseService;
import edu.ndsu.cs.estimate.services.tasks.TaskInterface;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Add {

    @Inject
    private EventDatabaseService eventDatabaseService;

    @Property
	@Persist
	EventInterface event;
    
    @Component
    private Form eventForm;

    @Inject
    private AlertManager alertManager;
    
    void setupRender() {
    	try {
		event = eventDatabaseService.getNewEvent();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date(); 
		String dateString = sdf.format(date);
		event.setCreatedDate(sdf.parse(dateString));
    	}
		catch (ParseException e) {
    		return;
    	}
	}

    void onValidateFromEventForm() {
    	List<String> errors = event.validate();
		for(String error : errors) {
			eventForm.recordError(error);
		}	
		if(!eventForm.getHasErrors()) {
			eventDatabaseService.updateEvent(event);
		}
			
    }

    Object onSuccessFromEventForm() {
        alertManager.alert(Duration.SINGLE, Severity.SUCCESS, "Event added successfully.");
        return Index.class;
    }
}
