package edu.ndsu.cs.estimate.services.hours;
import java.util.List;
import java.util.Map;

import org.apache.cayenne.Cayenne;
//import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
//import org.apache.tapestry5.annotations.Persist;
//import org.apache.tapestry5.annotations.Property;
//import org.apache.tapestry5.annotations.SessionAttribute;

import edu.ndsu.cs.estimate.services.hours.HoursInterface;
import edu.ndsu.cs.estimate.services.tasks.TaskInterface;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;
import edu.ndsu.cs.estimate.cayenne.persistent.Hours;
import edu.ndsu.cs.estimate.cayenne.persistent.Task;



public class CayenneHoursDatabaseService implements HoursDatabaseService{

	private CayenneService cayenneService;
	
	private Map<Integer, HoursInterface> hoursMap;
	
	
	public CayenneHoursDatabaseService(CayenneService cayenneService) {
		this.cayenneService = cayenneService; 
	}
	
	public CayenneService getCayenneService() {
		return cayenneService;
	}

	@Override
	public List<? extends HoursInterface> listAllHoursByTask(TaskInterface newTask) {
		return ObjectSelect.query(HoursInterface.class)
				.where(Hours.TASKS.eq((Task) newTask))
				.select(cayenneService.newContext());
	}

	@Override
	public HoursInterface getHours(int PK) {
		HoursInterface hours = hoursMap.get(PK);
		if (hours != null) {
			hours = Cayenne.objectForPK(cayenneService.newContext(), HoursInterface.class, PK);
		}
		return hours;
	}

	@Override
	public HoursInterface getNewHours() {
		return cayenneService.newContext().newObject(HoursInterface.class);
	}

	@Override
	public void deleteHours(int PK) {
//		ObjectContext context = cayenneService.newContext();
//		Hours hours = Cayenne.objectForPK(context, Hours.class, PK);
//		context.deleteObject(hours);
//		context.commitChanges();
		hoursMap.remove(PK);
	}

	@Override
	public void updateHours(HoursInterface hours) {
//		((Hours)hours).getObjectContext().commitChanges();
		hoursMap.put(hours.getPK(), hours);
	}
	
	

}

