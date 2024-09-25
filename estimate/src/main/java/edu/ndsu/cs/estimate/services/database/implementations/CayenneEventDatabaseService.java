package edu.ndsu.cs.estimate.services.database.implementations;

import edu.ndsu.cs.estimate.cayenne.persistent.Event;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;
import edu.ndsu.cs.estimate.services.database.interfaces.EventDatabaseService;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ColumnSelect;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.cayenne.query.SelectById;
import org.apache.cayenne.exp.Property;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CayenneEventDatabaseService implements EventDatabaseService {

    private final CayenneService cayenneService;

    public CayenneEventDatabaseService(CayenneService cayenneService) {
        this.cayenneService = cayenneService;
    }

    @Override
    public List<Event> findAllEvents() {
        ObjectContext context = cayenneService.newContext();
        return ObjectSelect.query(Event.class).select(context);
    }

    @Override
    public Event findEventById(int eventId) {
        ObjectContext context = cayenneService.newContext();
        return SelectById.query(Event.class, eventId).selectOne(context);
    }
    
    @Override
    public List<Event> findEventsInRange(Date start, Date end, String category) {
        ObjectContext context = cayenneService.newContext();

        Property<Date> eventDateProperty = Event.EVENT_DATE;
        Property<String> eventNameProperty = Event.NAME;
        Property<String> eventCategoryProperty = Event.CATEGORY;
        
        ObjectSelect<Event> query = ObjectSelect.query(Event.class)
            .where(eventDateProperty.between(start, end))
            .orderBy(eventDateProperty.asc(), eventNameProperty.asc());
        
        if (category != null && !category.isEmpty() && !category.equals("All Categories")) {
            query = query.and(eventCategoryProperty.eq(category));
        }
        
        return query.select(context);
    }
    
    @Override
    public List<String> findAllCategoriesInRange(Date start, Date end) {
        ObjectContext context = cayenneService.newContext();
        Property<Date> eventDateProperty = Event.EVENT_DATE;
        
        List<Event> events = ObjectSelect.query(Event.class)
                .where(eventDateProperty.between(start, end))
                .select(context);

        if (events == null || events.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> categorySet = new HashSet<>();
        for (Event event : events) {
            categorySet.add(event.getCategory());
        }

        return new ArrayList<>(categorySet);
    }


    @Override
    public Event createEvent(String name, String description, String category, Date eventDate) {
        ObjectContext context = cayenneService.newContext();
        Event event = context.newObject(Event.class);
        event.setName(name);
        event.setDescription(description);
        event.setCategory(category);
        event.setCreatedDate(new Date());
        event.setEventDate(eventDate); 
        context.commitChanges();
        return event;
    }

    @Override
    public Event updateEvent(int eventId, String name, String description, String category, Date eventDate) {
        ObjectContext context = cayenneService.newContext();
        Event event = SelectById.query(Event.class, eventId).selectOne(context);
        if (event != null) {
            event.setName(name);
            event.setDescription(description);
            event.setCategory(category);
            event.setEventDate(eventDate); 
            context.commitChanges();
        }
        return event;
    }

    @Override
    public void deleteEvent(int eventId) {
        ObjectContext context = cayenneService.newContext();
        Event event = SelectById.query(Event.class, eventId).selectOne(context);
        if (event != null) {
            context.deleteObject(event);
            context.commitChanges();
        }
    }
    
    @Override
    public Event updateEventResult(int eventId, Integer result) {
        ObjectContext context = cayenneService.newContext();
        Event event = SelectById.query(Event.class, eventId).selectOne(context);
        if (event != null) {
            event.setResult(result);
            context.commitChanges();
        }
        return event;
    }
}