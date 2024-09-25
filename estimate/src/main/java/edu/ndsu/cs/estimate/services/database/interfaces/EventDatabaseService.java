package edu.ndsu.cs.estimate.services.database.interfaces;

import edu.ndsu.cs.estimate.cayenne.persistent.Event;

import java.util.Date;
import java.util.List;

public interface EventDatabaseService {
    List<Event> findAllEvents();
    Event findEventById(int eventId);
    List<Event> findEventsInRange(Date start, Date end, String category);
    List<String> findAllCategoriesInRange(Date start, Date end);
    Event createEvent(String name, String description, String category, Date eventDate);
    Event updateEvent(int eventId, String name, String description, String category, Date eventDate);
    void deleteEvent(int eventId);
    Event updateEventResult(int eventId, Integer result);
}
