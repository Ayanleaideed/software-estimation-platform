package edu.ndsu.cs.estimate.pages.events;

import edu.ndsu.cs.estimate.cayenne.persistent.Entry;
import edu.ndsu.cs.estimate.services.EventService;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

import java.util.List;

public class Index {

    @Inject
    private EventService eventService;

    @Property
    private List<Entry> entries;

    @Property
    private Entry entry;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private Entry newEntry;

    @Component
    private Form createForm;

    @Component(id = "name")
    private TextField nameField;

    @Component(id = "description")
    private TextField descriptionField;

    void setupRender() {
        entries = eventService.findAllEntries();
        if (newEntry == null) {
            newEntry = new Entry();
        }
    }

    void onPrepareForRenderFromCreateForm() {
        if (newEntry == null) {
            newEntry = new Entry();
        }
    }

    void onPrepareForSubmitFromCreateForm() {
        if (newEntry == null) {
            newEntry = new Entry();
        }
    }

    void onValidateFromCreateForm() {
        if (createForm.getHasErrors()) {
            return;
        }
        try {
            eventService.createEntry(newEntry.getName(), newEntry.getDescription(), newEntry.getCategory());
        } catch (Exception e) {
            createForm.recordError("Error creating entry: " + e.getMessage());
        }
    }

    void onSuccessFromCreateForm() {
        newEntry = new Entry();
    }

    void onActionFromDelete(int entryId) {
        try {
            eventService.deleteEntry(entryId);
        } catch (Exception e) {
        }
    }

    Object onActionFromEdit(int entryId) {
    	//Later
        return null;
    }
}
