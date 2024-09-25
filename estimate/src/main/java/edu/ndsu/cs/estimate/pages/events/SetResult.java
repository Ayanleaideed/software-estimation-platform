package edu.ndsu.cs.estimate.pages.events;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.alerts.AlertManager;
import edu.ndsu.cs.estimate.cayenne.persistent.Event;
import edu.ndsu.cs.estimate.services.database.interfaces.EventDatabaseService;

public class SetResult {

    @Inject
    private EventDatabaseService eventDatabaseService;

    @InjectComponent
    private Form resultForm;

    @Inject
    private AlertManager alertManager;

    @Property
    private Event event;

    @Property
    @PageActivationContext
    private int eventId;

    @Property
    private Integer result;

    @Component(id = "resultSlider")
    private TextField resultField;
    

    void onActivate(int eventId) {
        this.eventId = eventId;
        event = eventDatabaseService.findEventById(eventId);
        if (event != null) {
            result = event.getResult();
        }
    }

    void onValidateFromResultForm() {
        if (result < 0 || result > 100) {
            resultForm.recordError(resultField, "Result must be between 0 and 100.");
            return;
        }

        eventDatabaseService.updateEventResult(eventId, result);
    }

    Object onSuccessFromResultForm() {
        alertManager.success("Result has been updated successfully.");
        return Index.class; 
    }

    Integer onPassivate() {
        return eventId;
    }
}
