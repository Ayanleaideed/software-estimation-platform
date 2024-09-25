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

import edu.ndsu.cs.estimate.cayenne.persistent.Hours;
import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;



public class CayenneHoursDatabaseService implements HoursDatabaseService{

	private CayenneService cayenneService;
	
	private Map<Integer, Hours> hoursMap;
	
	
	public CayenneHoursDatabaseService(CayenneService cayenneService) {
		this.cayenneService = cayenneService; 
	}
	
	public CayenneService getCayenneService() {
		return cayenneService;
	}

	@Override
	public List<? extends Hours> listAllHoursByTask(Task newTask) {
		return ObjectSelect.query(Hours.class)
				.where(Hours.TASKS.eq(newTask))
				.select(cayenneService.newContext());
	}

	@Override
	public Hours getHours(int PK) {
		Hours hours = hoursMap.get(PK);
		if (hours != null) {
			hours = Cayenne.objectForPK(cayenneService.newContext(), Hours.class, PK);
		}
		return hours;
	}

	@Override
	public Hours getNewHours() {
		return cayenneService.newContext().newObject(Hours.class);
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
	public void updateHours(Hours hours) {
//		((Hours)hours).getObjectContext().commitChanges();
		hoursMap.put(hours.getPK(), hours);
	}
	
	

}

