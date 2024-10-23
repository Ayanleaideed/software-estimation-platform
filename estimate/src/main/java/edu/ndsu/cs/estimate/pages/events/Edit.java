package edu.ndsu.cs.estimate.pages.events;

import edu.ndsu.cs.estimate.entities.interfaces.EventInterface;
import edu.ndsu.cs.estimate.services.database.interfaces.EventDatabaseService;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.PageLoaded;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Edit {

    @Inject
    private EventDatabaseService eventDatabaseService;

    @Property
    @Persist
    private EventInterface event;

    @Property
    private Integer eventPK;
    
    @Inject
    private AlertManager alertManager;

    @InjectComponent
    private Form eventForm;
    
    void setupRender() {
    	if(eventPK != null) {
			event = eventDatabaseService.getEvent(eventPK);
		}
    	
        //if (task != null) {
        //   hours = hoursDatabase.listAllHoursByTask(task);
        //    noHours = hours.isEmpty();
        //} else {
        //    noHours = true;
        //}
    }

    void onActivate(int PK) {
        this.eventPK = PK;
        //event = eventDatabaseService.findEventById(eventId);
        //if (event != null) {
        //    eventDateString = new SimpleDateFormat("MM/dd/yyyy").format(event.getEventDate());
        //}
    }
    
    Integer onPassivate() {
        return eventPK;
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
