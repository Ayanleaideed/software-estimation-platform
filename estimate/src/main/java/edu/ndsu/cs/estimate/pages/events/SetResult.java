package edu.ndsu.cs.estimate.pages.events;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

import org.apache.tapestry5.alerts.AlertManager;
import edu.ndsu.cs.estimate.entities.interfaces.EventInterface;
import edu.ndsu.cs.estimate.services.database.interfaces.EventDatabaseService;

public class SetResult {

    @Inject
    private EventDatabaseService eventDatabaseService;

    @InjectComponent
    private Form resultForm;

    @Inject
    private AlertManager alertManager;

    @Property
    @Persist
    private EventInterface event;

    @Property
    private Integer eventPK;

    @Property
    private boolean eventOutcomeBoolean;
    

    void setupRender() {
    	if(eventPK != null) {
			event = eventDatabaseService.getEvent(eventPK);
		}
        if (event != null) {
        	if (event.getResult() == 100) {
        		eventOutcomeBoolean = true;
        	}
        	else {
        		eventOutcomeBoolean = false;
        	}
        }
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

    void onValidateFromResultForm() {
    	if(eventOutcomeBoolean = true) {
    		event.setResult(100);
    	}
    	else {
    		event.setResult(0);
    	}
    	List<String> errors = event.validate();
		for(String error : errors) {
			resultForm.recordError(error);
		}	
		if(!resultForm.getHasErrors()) {
			eventDatabaseService.updateEvent(event);
		}
    }

    Object onSuccessFromResultForm() {
        alertManager.success("Result has been updated successfully.");
        return Index.class; 
    }
}
