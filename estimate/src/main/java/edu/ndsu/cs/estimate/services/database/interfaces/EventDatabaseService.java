package edu.ndsu.cs.estimate.services.database.interfaces;

import edu.ndsu.cs.estimate.entities.interfaces.EventInterface;
import edu.ndsu.cs.estimate.services.tasks.TaskInterface;

import java.util.Date;
import java.util.List;

public interface EventDatabaseService {
    List<? extends EventInterface> findAllEvents();
    EventInterface findEventById(int eventId);
    List<? extends EventInterface> findEventsInRange(Date start, Date end, String category);
    List<String> findAllCategoriesInRange(Date start, Date end);
    EventInterface createEvent(String name, String description, String category, Date eventDate);
    void updateEvent(EventInterface event);
    void deleteEvent(int eventId);
    EventInterface updateEventResult(int eventId, Integer result);
    public EventInterface	getNewEvent(); 
    public EventInterface	getEvent(int PK); 
}
