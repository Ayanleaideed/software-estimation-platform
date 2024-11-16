package edu.ndsu.cs.estimate.services.hours;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.query.ObjectSelect;
import edu.ndsu.cs.estimate.services.tasks.TaskInterface;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;
import edu.ndsu.cs.estimate.cayenne.persistent.Hours;
import edu.ndsu.cs.estimate.cayenne.persistent.Task;

public class CayenneHoursDatabaseService implements HoursDatabaseService {

    private final CayenneService cayenneService;

    private final Map<Integer, HoursInterface> hoursMap = new HashMap<>();

    public CayenneHoursDatabaseService(CayenneService cayenneService) {
        this.cayenneService = cayenneService;
    }

    public CayenneService getCayenneService() {
        return cayenneService;
    }

    @Override
    public List<? extends HoursInterface> listAllHoursByTask(TaskInterface task) {
        var context = cayenneService.newContext();
        List<? extends HoursInterface> hoursList = ObjectSelect.query(Hours.class)
                .where(Hours.TASKS.eq((Task) task))
                .select(context);

        // Populate hoursMap with retrieved hours
        hoursMap.clear();
        for (HoursInterface hour : hoursList) {
            hoursMap.put(hour.getPK(), hour);
        }

        return hoursList;
    }

    @Override
    public HoursInterface getHours(int PK) {
        HoursInterface hours = hoursMap.get(PK);
        if (hours == null) {
            hours = Cayenne.objectForPK(cayenneService.newContext(), Hours.class, PK);
            if (hours != null) {
                hoursMap.put(PK, hours);
            }
        }
        return hours;
    }

    @Override
    public HoursInterface getNewHours(TaskInterface task) {
        var context = ((Task) task).getObjectContext();                     // Ensure same context as task
        Hours newHour = context.newObject(Hours.class);     // Create Hours in task's context
        return newHour;
    }

    @Override
    public void deleteHours(int PK) {
        HoursInterface hours = getHours(PK);
        if (hours != null) {
            var context = ((Hours) hours).getObjectContext();
            context.deleteObject((Hours) hours);
            context.commitChanges();
            hoursMap.remove(PK);
        }
    }

    @Override
    public void updateHours(HoursInterface hours) {
        var context = ((Hours) hours).getObjectContext();
        context.commitChanges();
        hoursMap.put(hours.getPK(), hours); // Cache updated hour after commit
    }
    
    @Override
    public int sumOfHoursForTask(TaskInterface task) {
        var context = cayenneService.newContext();
        List<? extends HoursInterface> hoursList = ObjectSelect.query(Hours.class)
                .where(Hours.TASKS.eq((Task) task))
                .select(context);
        
        // Sum total hours of a task
        int totalHours = 0;
        for (HoursInterface hour : hoursList) {
            totalHours += hour.getHoursLogged();
        }

        return totalHours;
    }
}
